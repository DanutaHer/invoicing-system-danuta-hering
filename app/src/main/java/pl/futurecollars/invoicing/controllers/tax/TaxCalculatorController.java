package pl.futurecollars.invoicing.controllers.tax;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.TaxCalculator;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@AllArgsConstructor
@RestController
@Slf4j
public class TaxCalculatorController implements TaxCalculatorApi {

    private final TaxCalculatorService taxCalculatorService;

    @Override
    public ResponseEntity<TaxCalculator> getCalculateTaxes(String taxIdentificationNumber) {
        TaxCalculator taxCalculator = taxCalculatorService.calculateTaxes(taxIdentificationNumber);
            log.info("Get calculate tax with id: " + taxIdentificationNumber);
            return ResponseEntity.ok(taxCalculator);
        }
    }

