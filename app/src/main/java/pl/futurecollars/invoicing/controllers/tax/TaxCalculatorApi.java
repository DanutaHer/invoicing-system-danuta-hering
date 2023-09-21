package pl.futurecollars.invoicing.controllers.tax;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.TaxCalculator;

@Api(tags = "Tax calculator controller")
@RequestMapping("invoices/taxCalculator")
public interface TaxCalculatorApi {

    @ApiOperation(value = "Method calculate tax: incomes, costs, vat and taxes to pay")
    @PostMapping
    ResponseEntity<TaxCalculator> getCalculateTaxes(@RequestBody Company company);
}
