package pl.futurecollars.invoicing.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.TaxCalculator;

@Service
public class TaxCalculatorService {

    private TaxCalculator taxCalculator;

    public Optional<TaxCalculator> calculateTaxes(String taxIdentificationNumber) {

        return Optional.ofNullable(null);
    }
}
