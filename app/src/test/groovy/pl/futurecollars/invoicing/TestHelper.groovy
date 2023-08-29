package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.*

import java.time.LocalDate

import static pl.futurecollars.invoicing.model.Vat.VAT_5

class TestHelper {

    static company(long id) {
        Company.builder()
                .id(id)
                .name("Name xyz")
                .address("ul. Zielona 17, 01-240 Warszawa")
                .taxIdentificationNumber("123-45-67-89")
                .pensionInsurance(new BigDecimal("1.00"))
                .healthInsurance(new BigDecimal("1.00"))
                .build()
    }

    static product(long id) {
        InvoiceEntry.builder()
                .description("Descr xyz")
                .price(new BigDecimal("1.00"))
                .vatValue(new BigDecimal("1.00"))
                .vatRate(VAT_5)
                .expenseRelatedToCar(Car.builder()
                        .registrationNumber("MND 1235")
                        .personalUse(false)
                        .build())
                .build();
    }

    static invoice(long id) {
        Invoice.builder()
                .id(id)
                .number("2023/06/15/0001")
                .date(LocalDate.now())
                .buyer(company(id))
                .seller(company(id))
                .entries(List.of(product(id)))
                .build();
    }

    static taxCalculator() {
        TaxCalculator.builder()
                .incomingVat(new BigDecimal("0"))
                .outgoingVat(new BigDecimal("0"))
                .income(new BigDecimal("0"))
                .costs(new BigDecimal("0"))
                .incomeMinusCosts(new BigDecimal("0"))
                .pensionInsurance(new BigDecimal("1.00"))
                .incomeMinusCostsMinusPensionInsurance(new BigDecimal("-1.00"))
                .incomeMinusCostsMinusPensionInsuranceRounded(new BigDecimal("-1"))
                .incomeTax(new BigDecimal("-0.19"))
                .healthInsurancePaid(new BigDecimal("1.00"))
                .healthInsuranceToSubtract(new BigDecimal("0.86"))
                .incomeTaxMinusHealthInsurance(new BigDecimal("-1.05"))
                .finalIncomeTax(new BigDecimal("-1"))
                .vatToPay(new BigDecimal("0"))
                .build();
    }
}
