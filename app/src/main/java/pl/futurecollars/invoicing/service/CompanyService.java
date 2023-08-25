package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Database<Company> database;

    public long save(Company company) {
        return database.save(company);
    }

    public Optional<Company> getByID(long id) {
        return database.getByID(id);
    }

    public List<Company> getAll() {
        return database.getAll();
    }

    public Optional<Company> update(long id, Company updatedCompany) {
        return database.update(id, updatedCompany);
    }

    public Optional<Company> delete(long id) {
        return database.delete(id);
    }

}
