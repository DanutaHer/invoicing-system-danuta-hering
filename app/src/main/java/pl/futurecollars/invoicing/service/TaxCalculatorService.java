package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.TaxCalculator;

@AllArgsConstructor
@Service
public class TaxCalculatorService {

    private final Database<Invoice> database;

    BigDecimal income(String taxIdentificationNumber) {
        return visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getNetPrice);
    }

    BigDecimal costs(String taxIdentificationNumber) {
        return visit(buyerPredicate(taxIdentificationNumber), this::getIncomeValueTakingIntoConsiderationPersonalCarUsage);
    }

    BigDecimal incomingVat(String taxIdentificationNumber) {
        return visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    BigDecimal outgoingVat(String taxIdentificationNumber) {
        return visit(buyerPredicate(taxIdentificationNumber), this::getVatValueTakingIntoConsiderationPersonalCarUsage);
    }

    BigDecimal getEarnings(String taxIdentificationNumber) {
        return income(taxIdentificationNumber).subtract(costs(taxIdentificationNumber));
    }

    BigDecimal getVatToPay(String taxIdentificationNumber) {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
    }

    public TaxCalculator calculateTaxes(Company company) {
        String taxIdentificationNumber = company.getTaxIdentificationNumber();

        BigDecimal incomeMinusCosts = getEarnings(taxIdentificationNumber);
        BigDecimal incomeMinusCostsMinusPensionInsurance = incomeMinusCosts.subtract(company.getPensionInsurance());
        BigDecimal incomeMinusCostsMinusPensionInsuranceRounded = incomeMinusCostsMinusPensionInsurance.setScale(0, RoundingMode.HALF_DOWN);
        BigDecimal incomeTax = incomeMinusCostsMinusPensionInsuranceRounded.multiply(BigDecimal.valueOf(19, 2));
        BigDecimal healthInsuranceToSubtract =
            company.getHealthInsurance().multiply(BigDecimal.valueOf(775)).divide(BigDecimal.valueOf(900), RoundingMode.HALF_UP);
        BigDecimal incomeTaxMinusHealthInsurance = incomeTax.subtract(healthInsuranceToSubtract);

        return TaxCalculator.builder()
            .income(income(taxIdentificationNumber))
            .costs(costs(taxIdentificationNumber))
            .incomeMinusCosts(incomeMinusCosts)
            .pensionInsurance(company.getPensionInsurance())
            .incomeMinusCostsMinusPensionInsurance(incomeMinusCostsMinusPensionInsurance)
            .incomeMinusCostsMinusPensionInsuranceRounded(incomeMinusCostsMinusPensionInsuranceRounded)
            .incomeTax(incomeTax)
            .healthInsurancePaid(company.getHealthInsurance())
            .healthInsuranceToSubtract(healthInsuranceToSubtract)
            .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthInsurance)
            .finalIncomeTax(incomeTaxMinusHealthInsurance.setScale(0, RoundingMode.DOWN))
            // vat
            .incomingVat(incomingVat(taxIdentificationNumber))
            .outgoingVat(outgoingVat(taxIdentificationNumber))
            .vatToPay(getVatToPay(taxIdentificationNumber))
            .build();
    }

    private BigDecimal getIncomeValueTakingIntoConsiderationPersonalCarUsage(InvoiceEntry invoiceEntry) {
        return invoiceEntry.getNetPrice()
            .add(invoiceEntry.getVatValue())
            .subtract(getVatValueTakingIntoConsiderationPersonalCarUsage(invoiceEntry));
    }

    private BigDecimal getVatValueTakingIntoConsiderationPersonalCarUsage(InvoiceEntry invoiceEntry) {
        return Optional.ofNullable(invoiceEntry.getExpenseRelatedToCar())
            .map(Car::isPersonalUse)
            .map(personalCarUsage -> personalCarUsage ? BigDecimal.valueOf(5, 1) : BigDecimal.ONE)
            .map(proportion -> invoiceEntry.getVatValue().multiply(proportion))
            .map(value -> value.setScale(2, RoundingMode.FLOOR))
            .orElse(invoiceEntry.getVatValue());
    }

    private Predicate<Invoice> buyerPredicate(String taxIdentificationNumber) {
        return invoice -> invoice.getBuyer().getTaxIdentificationNumber().equals(taxIdentificationNumber);
    }

    private Predicate<Invoice> sellerPredicate(String taxIdentificationNumber) {
        return invoice -> invoice.getSeller().getTaxIdentificationNumber().equals(taxIdentificationNumber);
    }

    BigDecimal visit(Predicate<Invoice> predicateInvoice, Function<InvoiceEntry, BigDecimal> invoiceEntryToAmount) {
        return database.getAll().stream()
            .filter(predicateInvoice)
            .flatMap(invoice -> invoice.getEntries().stream())
            .map(invoiceEntryToAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
