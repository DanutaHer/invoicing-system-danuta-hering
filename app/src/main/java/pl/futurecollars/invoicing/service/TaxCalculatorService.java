package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.TaxCalculator;

@AllArgsConstructor
@Service
public class TaxCalculatorService {

    private final Database database;

    BigDecimal income(String taxIdentificationNumber) {
        return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
    }

    BigDecimal costs(String taxIdentificationNumber) {
        return database.visit(buyerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
    }

    BigDecimal incomingVat(String taxIdentificationNumber) {
        return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    BigDecimal outgoingVat(String taxIdentificationNumber) {
        return database.visit(buyerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    BigDecimal getEarnings(String taxIdentificationNumber) {
        return income(taxIdentificationNumber).subtract(costs(taxIdentificationNumber));
    }

    BigDecimal getVatToPay(String taxIdentificationNumber) {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
    }

    public TaxCalculator calculateTaxes(String taxIdentificationNumber) {
        return TaxCalculator.builder()
                .income(income(taxIdentificationNumber))
                .costs(costs(taxIdentificationNumber))
                .incomingVat(incomingVat(taxIdentificationNumber))
                .outgoingVat(outgoingVat(taxIdentificationNumber))
                .earnings(getEarnings(taxIdentificationNumber))
                .vatToPay(getVatToPay(taxIdentificationNumber))
                .build();
    }

    private Predicate<Invoice> buyerPredicate(String taxIdentificationNumber) {
        return invoice -> invoice.getBuyer().getTaxIdentificationNumber().equals(taxIdentificationNumber);
    }

    private Predicate<Invoice> sellerPredicate(String taxIdentificationNumber) {
        return invoice -> invoice.getSeller().getTaxIdentificationNumber().equals(taxIdentificationNumber);
    }
}
