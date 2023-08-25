package pl.futurecollars.invoicing.controllers.company;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.CompanyService;

@AllArgsConstructor
@RestController
@Slf4j
public class CompanyController implements CompanyApi {

    private final CompanyService companyService;

    @Override
    public ResponseEntity<List<Company>> getAll() {
        List<Company> allCompanies = companyService.getAll();
        log.info("Get the count of all companies: " + allCompanies.size());
        return ResponseEntity.ok(allCompanies);
    }

    @Override
    public ResponseEntity<Long> add(@RequestBody Company company) {
        log.info("Post new company: " + company);
        return ResponseEntity.ok(companyService.save(company));
    }

    @Override
    public ResponseEntity<Company> getById(@PathVariable long id) {
        Optional<Company> optionalCompany = companyService.getByID(id);
        if (optionalCompany.isPresent()) {
            log.info("Get company with id: " + id);
            return ResponseEntity.ok(optionalCompany.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Company> updateById(@PathVariable long id, @RequestBody Company company) {
        Optional<Company> optionalCompany = companyService.update(id, company);
        if (optionalCompany.isPresent()) {
            log.info("Update company with id: " + id);
            return ResponseEntity.ok(optionalCompany.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Company> deleteById(@PathVariable long id) {
        Optional<Company> optionalCompany = companyService.delete(id);
        if (optionalCompany.isPresent()) {
            log.info("Delete company with id: " + id);
            return ResponseEntity.ok(optionalCompany.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
