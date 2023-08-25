package pl.futurecollars.invoicing.db.sql;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import pl.futurecollars.invoicing.model.Company;

@AllArgsConstructor
public class AbstractSqlDatabase {

    protected JdbcTemplate jdbcTemplate;

    protected void updateCompany(Company companyToUpdate, Long originalCompanyId) {
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
            ps.setLong(6, originalCompanyId);
            return ps;
        });
    }

    protected long insertCompany(Company company) {
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

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    protected Company getCompany(long companyId) {
        List<Company> companyList = jdbcTemplate.query("select * from company c where c.id = " + companyId,
            (response, ignored) -> Company.builder()
                .id(response.getLong("id"))
                .name(response.getString("name"))
                .address(response.getString("address"))
                .taxIdentificationNumber(response.getString("tax_identification_number"))
                .pensionInsurance(response.getBigDecimal("pension_insurance"))
                .healthInsurance(response.getBigDecimal("health_insurance"))
                .build()
        );
        return companyList.get(0);
    }
}
