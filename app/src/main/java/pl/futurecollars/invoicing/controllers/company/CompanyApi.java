package pl.futurecollars.invoicing.controllers.company;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Company;

@CrossOrigin
@Api(tags = "Company controller")
@RequestMapping("companies")
public interface CompanyApi {

    @ApiOperation(value = "Method used to list all companies")
    @GetMapping
    ResponseEntity<List<Company>> getAll();

    @ApiOperation(value = "Method used to add company")
    @PostMapping
    ResponseEntity<Long> add(@RequestBody Company company);

    @ApiOperation(value = "Method used to get company by Id")
    @GetMapping("/{id}")
    ResponseEntity<Company> getById(@PathVariable long id);

    @ApiOperation(value = "Method used to update company by Id ")
    @PutMapping("/{id}")
    ResponseEntity<Company> updateById(@PathVariable long id, @RequestBody Company company);

    @ApiOperation(value = "Method used to delete company by Id")
    @DeleteMapping("/{id}")
    ResponseEntity<Company> deleteById(@PathVariable long id);
}
