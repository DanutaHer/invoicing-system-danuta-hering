package pl.futurecollars.invoicing.db.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;

@AllArgsConstructor
public class SqlDatabase implements Database {

    private final Map<Vat, Integer> vatToId = new HashMap<>();
    private final Map<Integer, Vat> idToVat = new HashMap<>();
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    void initVatRatesMap() {
        jdbcTemplate.query("select * from vat",
            rs -> {
                Vat vat = Vat.valueOf("VAT_" + rs.getString("name"));
                int id = rs.getInt("id");
                vatToId.put(vat, id);
                idToVat.put(id, vat);
            });
    }

    @Override
    @Transactional
    public int save(Invoice invoice) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int buyerId = insertCompany(invoice.getBuyer());
        int sellerId = insertCompany(invoice.getSeller());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement("insert into invoice (number, date, buyer, seller) values (?, ?, ?, ?);", new String[] {"id"});
            ps.setString(1, invoice.getNumber());
            ps.setDate(2, Date.valueOf(invoice.getDate()));
            ps.setInt(3, buyerId);
            ps.setInt(4, sellerId);
            return ps;
        }, keyHolder);

        int invoiceId = keyHolder.getKey().intValue();
        insertInvoiceEntry(invoiceId, invoice);

        return invoiceId;
    }

    @Override
    public Optional<Invoice> getByID(int id) {
        List<Invoice> invoiceList = jdbcTemplate.query("select * from invoice i where i.id = " + id, invoiceRowMapper());

        return invoiceList.isEmpty() ? Optional.empty() : Optional.of(invoiceList.get(0));
    }

    @Override
    public List<Invoice> getAll() {
        return jdbcTemplate.query("select * from invoice i ", invoiceRowMapper());
    }

    @Override
    public Optional<Invoice> update(int id, Invoice updatedInvoice) {
        Optional<Invoice> foundInvoice = getByID(id);
        if (foundInvoice.isEmpty()) {
            return Optional.empty();
        }

        Integer buyerId = jdbcTemplate.queryForObject("select buyer from invoice i where i.id = " + id, Integer.class);
        Integer sellerId = jdbcTemplate.queryForObject("select seller from invoice i where i.id = " + id, Integer.class);

        updateCompany(updatedInvoice.getBuyer(), buyerId);
        updateCompany(updatedInvoice.getSeller(), sellerId);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement(
                    "update invoice set number=?, date=? where id=?"
                );
            ps.setString(1, updatedInvoice.getNumber());
            ps.setDate(2, Date.valueOf(updatedInvoice.getDate()));
            ps.setInt(3, id);
            return ps;
        });

        jdbcTemplate.execute("delete from invoice_invoice_entry where invoice_id = " + id);
        insertInvoiceEntry(id, updatedInvoice);

        List<Invoice> invoiceList = jdbcTemplate.query("select * from invoice i where i.id = " + id, invoiceRowMapper());

        return invoiceList.isEmpty() ? Optional.empty() : Optional.of(invoiceList.get(0));
    }

    @Override
    public Optional<Invoice> delete(int id) {
        Optional<Invoice> invoiceToDelete = getByID(id);
        jdbcTemplate.execute("delete from invoice where id = " + id);
        return invoiceToDelete;
    }

    private void updateCompany(Company companyToUpdate, Integer originalCompanyId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "update company "
                    + "set name=?, "
                    + "address=?, "
                    + "tax_identification_number=?, "
                    + "health_insurance=?, "
                    + "pension_insurance=? "
                    + "where id=?"
            );
            ps.setString(1, companyToUpdate.getName());
            ps.setString(2, companyToUpdate.getAddress());
            ps.setString(3, companyToUpdate.getTaxIdentificationNumber());
            ps.setBigDecimal(4, companyToUpdate.getHealthInsurance());
            ps.setBigDecimal(5, companyToUpdate.getPensionInsurance());
            ps.setInt(6, originalCompanyId);
            return ps;
        });
    }

    private int insertCompany(Company company) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into company (name, address, tax_identification_number, pension_insurance, health_insurance) values (?, ?, ?, ?, ?);",
                new String[] {"id"});
            ps.setString(1, company.getName());
            ps.setString(2, company.getAddress());
            ps.setString(3, company.getTaxIdentificationNumber());
            ps.setBigDecimal(4, company.getPensionInsurance());
            ps.setBigDecimal(5, company.getHealthInsurance());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    private void insertInvoiceEntry(int invoiceId, Invoice invoice) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        invoice.getEntries().forEach(entry ->
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into invoice_entry (description, price, vat_value, vat_rate, expense_related_to_car) values (?, ?, ?, ?, ?);",
                    new String[] {"id"});
                ps.setString(1, entry.getDescription());
                ps.setBigDecimal(2, entry.getNetPrice());
                ps.setBigDecimal(3, entry.getVatValue());
                ps.setInt(4, vatToId.get(entry.getVatRate()));
                ps.setInt(5, insertCar(entry.getExpenseRelatedToCar()));
                return ps;
            }, keyHolder));

        int invoiceEntryId = keyHolder.getKey().intValue();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into invoice_invoice_entry (invoice_id, invoice_entry_id) values (?, ?);");
            ps.setInt(1, invoiceId);
            ps.setInt(2, invoiceEntryId);
            return ps;
        });
    }

    private int insertCar(Car car) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into car (registration_number, personal_use) values (?, ?);",
                new String[] {"id"});
            ps.setString(1, car.getRegistrationNumber());
            ps.setBoolean(2, car.isPersonalUse());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    private RowMapper<Invoice> invoiceRowMapper() {
        return (rs, rowNum) -> {
            int invoiceId = rs.getInt("id");
            int buyerId = rs.getInt("buyer");
            int sellerId = rs.getInt("seller");

            return Invoice.builder()
                .id(rs.getInt("id"))
                .number(rs.getString("number"))
                .date(rs.getDate("date").toLocalDate())
                .buyer(getBuyer(buyerId))
                .seller(getSeller(sellerId))
                .entries(getInvoiceEntry(invoiceId))
                .build();
        };
    }

    private List<InvoiceEntry> getInvoiceEntry(int invoiceId) {
        return jdbcTemplate.query("select * from invoice_invoice_entry iie "
                + "inner join invoice_entry ie on iie.invoice_entry_id = ie.id "
                + "left outer join car c on ie.expense_related_to_car = c.id "
                + "where invoice_id = " + invoiceId,
            (response, ignored) -> InvoiceEntry.builder()
                .description(response.getString("description"))
                .netPrice(response.getBigDecimal("price"))
                .vatValue(response.getBigDecimal("vat_value"))
                .vatRate(idToVat.get(response.getInt("vat_rate")))
                .expenseRelatedToCar(response.getObject("registration_number") != null ? Car.builder()
                    .registrationNumber(response.getString("registration_number"))
                    .personalUse(response.getBoolean("personal_use"))
                    .build()
                    : null)
                .build()

        );
    }

    private Company getBuyer(int buyerId) {
        List<Company> buyerList = jdbcTemplate.query("select * from company c where c.id = " + buyerId,
            (response, ignored) -> Company.builder()
                .name(response.getString("name"))
                .address(response.getString("address"))
                .taxIdentificationNumber(response.getString("tax_identification_number"))
                .pensionInsurance(response.getBigDecimal("pension_insurance"))
                .healthInsurance(response.getBigDecimal("health_insurance"))
                .build()
        );
        return buyerList.get(0);
    }

    private Company getSeller(int sellerId) {
        List<Company> sellerList = jdbcTemplate.query("select * from company c where c.id = " + sellerId,
            (response, ignored) -> Company.builder()
                .name(response.getString("name"))
                .address(response.getString("address"))
                .taxIdentificationNumber(response.getString("tax_identification_number"))
                .pensionInsurance(response.getBigDecimal("pension_insurance"))
                .healthInsurance(response.getBigDecimal("health_insurance"))
                .build()
        );
        return sellerList.get(0);
    }
}
