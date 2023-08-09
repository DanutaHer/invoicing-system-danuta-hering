package pl.futurecollars.invoicing.controllers.tax;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.TaxCalculator;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@AllArgsConstructor
@RestController
@Slf4j
public class TaxCalculatorController implements TaxCalculatorApi {

    private final TaxCalculatorService taxCalculatorService;

    @Override
    public ResponseEntity<Optional<TaxCalculator>> getCalculateTaxes(@PathVariable("taxIdentificationNumber") String taxIdentificationNumber) {
        Optional<TaxCalculator> optionalTaxCalculator = taxCalculatorService.calculateTaxes(taxIdentificationNumber);
        if (optionalTaxCalculator.isPresent()) {
            log.info("Get invoice with id: " + taxIdentificationNumber);
            return ResponseEntity.ok(optionalTaxCalculator);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
