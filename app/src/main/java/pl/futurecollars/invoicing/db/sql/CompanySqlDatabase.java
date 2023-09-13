package pl.futurecollars.invoicing.db.sql;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

public class CompanySqlDatabase extends AbstractSqlDatabase implements Database<Company> {

    public CompanySqlDatabase(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public long save(Company company) {
        return insertCompany(company);
    }

    @Override
    public Optional<Company> getByID(long id) {
        List<Company> companyList = jdbcTemplate.query("select * from company c where c.id = " + id, companyRowMapper());
        return companyList.isEmpty() ? Optional.empty() : Optional.of(companyList.get(0));
    }

    @Override
    public List<Company> getAll() {
        return jdbcTemplate.query("select * from company c ", companyRowMapper());
    }

    @Override
    public Optional<Company> update(long id, Company updatedCompany) {
        Optional<Company> foundCompany = getByID(id);
        if (foundCompany.isEmpty()) {
            return Optional.empty();
        }

        updateCompany(updatedCompany, id);
        return getByID(id);
    }

    @Override
    public Optional<Company> delete(long id) {
        Optional<Company> company = getByID(id);
        jdbcTemplate.execute("delete from company where id = " + id);
        return company;
    }

    private RowMapper<Company> companyRowMapper() {
        return (rs, rowNum) -> Company.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .address(rs.getString("address"))
            .taxIdentificationNumber(rs.getString("tax_identification_number"))
            .pensionInsurance(rs.getBigDecimal("pension_insurance"))
            .healthInsurance(rs.getBigDecimal("health_insurance"))
            .build();
    }
}
