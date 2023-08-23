package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.*

import java.time.LocalDate

import static pl.futurecollars.invoicing.model.Vat.VAT_5

class TestHelper {

    static company(int id) {
        Company.builder()
                .name("Name $id")
                .address("ul. Zielona 17, 01-240 Warszawa")
                .taxIdentificationNumber("123-45-67-89")
                .pensionInsurance(new BigDecimal("1.00"))
                .healthInsurance(new BigDecimal("1.00"))
                .build()
    }

    static product(int id) {
        InvoiceEntry.builder()
                .description("Descr $id")
                .price(new BigDecimal("1.00"))
                .vatValue(new BigDecimal("1.00"))
                .vatRate(VAT_5)
                .expenseRelatedToCar(car())
                .build();
    }

    static invoice(int id) {
        Invoice.builder()
                .id(id)
                .date(LocalDate.now())
                .buyer(company(id))
                .seller(company(id))
                .entries(List.of(product(id)))
                .build();
    }

    static taxCalculator() {
        TaxCalculator.builder()
                .incomingVat(BigDecimal.valueOf(0))
                .outgoingVat(BigDecimal.valueOf(0))
                .income(BigDecimal.valueOf(0))
                .costs(BigDecimal.valueOf(0))
                .incomeMinusCosts(BigDecimal.valueOf(0))
                .pensionInsurance(1.00)
                .incomeMinusCostsMinusPensionInsurance(-1.00)
                .incomeMinusCostsMinusPensionInsuranceRounded(BigDecimal.valueOf(-1))
                .incomeTax(-0.19)
                .healthInsurancePaid(1.00)
                .healthInsuranceToSubtract(0.86)
                .incomeTaxMinusHealthInsurance(-1.05)
                .finalIncomeTax(BigDecimal.valueOf(-1))
                .vatToPay(BigDecimal.valueOf(0))
                .build();
    }

    static car() {
        Car.builder()
                .registrationNumber("MBD 1245")
                .personalUse(false)
                .build()
    }
}
