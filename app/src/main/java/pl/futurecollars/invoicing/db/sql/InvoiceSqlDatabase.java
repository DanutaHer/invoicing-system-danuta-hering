package pl.futurecollars.invoicing.db.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;

public class InvoiceSqlDatabase extends AbstractSqlDatabase implements Database<Invoice> {

    public InvoiceSqlDatabase(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public long save(Invoice invoice) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        long buyerId = insertCompany(invoice.getBuyer());
        long sellerId = insertCompany(invoice.getSeller());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement("insert into invoice (number, date, buyer, seller) values (?, ?, ?, ?);", new String[] {"id"});
            ps.setString(1, invoice.getNumber());
            ps.setDate(2, Date.valueOf(invoice.getDate()));
            ps.setLong(3, buyerId);
            ps.setLong(4, sellerId);
            return ps;
        }, keyHolder);

        long invoiceId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        insertInvoiceEntry(invoiceId, invoice);

        return invoiceId;
    }

    @Override
    public Optional<Invoice> getByID(long id) {
        List<Invoice> invoiceList = jdbcTemplate.query("select * from invoice i where i.id = " + id, invoiceRowMapper());

        return invoiceList.isEmpty() ? Optional.empty() : Optional.of(invoiceList.get(0));
    }

    @Override
    public List<Invoice> getAll() {
        return jdbcTemplate.query("select * from invoice i ", invoiceRowMapper());
    }

    @Override
    @Transactional
    public Optional<Invoice> update(long id, Invoice updatedInvoice) {
        Long buyerId = jdbcTemplate.queryForObject("select buyer from invoice i where i.id = " + id, Long.class);
        Long sellerId = jdbcTemplate.queryForObject("select seller from invoice i where i.id = " + id, Long.class);

        updateCompany(updatedInvoice.getBuyer(), buyerId);
        updateCompany(updatedInvoice.getSeller(), sellerId);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement(
                    "update invoice set number=?, date=? where id=?"
                );
            ps.setString(1, updatedInvoice.getNumber());
            ps.setDate(2, Date.valueOf(updatedInvoice.getDate()));
            ps.setLong(3, id);
            return ps;
        });

        jdbcTemplate.execute("delete from invoice_invoice_entry where invoice_id = " + id);
        insertInvoiceEntry(id, updatedInvoice);

        List<Invoice> invoiceList = jdbcTemplate.query("select * from invoice i where i.id = " + id, invoiceRowMapper());

        return invoiceList.isEmpty() ? Optional.empty() : Optional.of(invoiceList.get(0));
    }

    @Override
    @Transactional
    public Optional<Invoice> delete(long id) {
        Optional<Invoice> invoiceToDelete = getByID(id);
        jdbcTemplate.execute("delete from invoice where id = " + id);
        return invoiceToDelete;
    }

    private void insertInvoiceEntry(long invoiceId, Invoice invoice) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        invoice.getEntries().forEach(entry ->
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into invoice_entry (description, price, vat_value, vat_rate, expense_related_to_car) values (?, ?, ?, ?, ?);",
                    new String[] {"id"});
                ps.setString(1, entry.getDescription());
                ps.setBigDecimal(2, entry.getPrice());
                ps.setBigDecimal(3, entry.getVatValue());
                ps.setString(4, entry.getVatRate().name());
                ps.setObject(5, insertCar(entry.getExpenseRelatedToCar()));
                return ps;
            }, keyHolder));

        long invoiceEntryId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into invoice_invoice_entry (invoice_id, invoice_entry_id) values (?, ?);");
            ps.setLong(1, invoiceId);
            ps.setLong(2, invoiceEntryId);
            return ps;
        });
    }

    private long insertCar(Car car) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into car (registration_number, personal_use) values (?, ?);",
                new String[] {"id"});
            ps.setString(1, car.getRegistrationNumber());
            ps.setBoolean(2, car.isPersonalUse());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private RowMapper<Invoice> invoiceRowMapper() {
        return (rs, rowNum) -> {
            long invoiceId = rs.getInt("id");
            long buyerId = rs.getInt("buyer");
            long sellerId = rs.getInt("seller");

            return Invoice.builder()
                .id(rs.getLong("id"))
                .number(rs.getString("number"))
                .date(rs.getDate("date").toLocalDate())
                .buyer(getCompany(buyerId))
                .seller(getCompany(sellerId))
                .entries(getInvoiceEntry(invoiceId))
                .build();
        };
    }

    private List<InvoiceEntry> getInvoiceEntry(long invoiceId) {
        return jdbcTemplate.query("select * from invoice_invoice_entry iie "
                + "inner join invoice_entry ie on iie.invoice_entry_id = ie.id "
                + "left outer join car c on ie.expense_related_to_car = c.id "
                + "where invoice_id = " + invoiceId,
            (response, ignored) -> InvoiceEntry.builder()
                .description(response.getString("description"))
                .price(response.getBigDecimal("price"))
                .vatValue(response.getBigDecimal("vat_value"))
                .vatRate(Vat.valueOf(response.getString("vat_rate")))
                .expenseRelatedToCar(response.getObject("registration_number") != null ? Car.builder()
                    .registrationNumber(response.getString("registration_number"))
                    .personalUse(response.getBoolean("personal_use"))
                    .build()
                    : null)
                .build()
        );
    }
}
