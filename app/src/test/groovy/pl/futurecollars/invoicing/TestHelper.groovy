package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.*

import java.time.LocalDate

import static pl.futurecollars.invoicing.model.Vat.VAT_21

class TestHelper {

    static company(long id) {
        Company.builder()
                .name("Name xyz")
                .address("ul. Zielona 17, 01-240 Warszawa")
                .taxIdentificationNumber("123-45-67-89")
                .pensionInsurance(new BigDecimal("514.57"))
                .healthInsurance(new BigDecimal("319.94"))
                .build()
    }

    static product(long id) {
        InvoiceEntry.builder()
                .description("Descr xyz")
                .netPrice(new BigDecimal("1500.00"))
                .vatValue(new BigDecimal("315.00"))
                .vatRate(VAT_21)
                .expenseRelatedToCar(Car.builder()
                        .registrationNumber("MND 1235")
                        .personalUse(true)
                        .build())
                .build()
    }

    static invoice(long id) {
        Invoice.builder()
                .id(id)
                .number("2023/06/15/0001")
                .date(LocalDate.now())
                .buyer(company(id))
                .seller(company(id))
                .entries(List.of(product(id)))
                .build()
    }

    static taxCalculator() {
        TaxCalculator.builder()
                .income(BigDecimal.valueOf(0))
                .costs(BigDecimal.valueOf(0))
                .incomeMinusCosts(BigDecimal.valueOf(0))
                .pensionInsurance(0.00)
                .incomeMinusCostsMinusPensionInsurance(0.00)
                .incomeMinusCostsMinusPensionInsuranceRounded(BigDecimal.valueOf(0))
                .incomeTax(0.00)
                .healthInsurancePaid(0.00)
                .healthInsuranceToSubtract(0.00)
                .incomeTaxMinusHealthInsurance(0.00)
                .finalIncomeTax(BigDecimal.valueOf(0))
                .incomingVat(BigDecimal.valueOf(0))
                .outgoingVat(BigDecimal.valueOf(0))
                .vatToPay(BigDecimal.valueOf(0))
                .build()
    }
}
