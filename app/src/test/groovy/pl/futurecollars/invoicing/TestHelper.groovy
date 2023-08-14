package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.TaxCalculator

import java.time.LocalDate

import static pl.futurecollars.invoicing.model.Vat.VAT_5

class TestHelper {

    static company(int id) {
        Company.builder()
                .name("Name $id")
                .address("ul. Zielona 17, 01-240 Warszawa")
                .taxIdentificationNumber("123-45-67-89")
                .build()
    }

    static product(int id) {
        InvoiceEntry.builder()
                .description("Descr $id")
                .price(BigDecimal.ONE)
                .vatValue(BigDecimal.ONE)
                .vatRate(VAT_5)
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

    static taxCalculator(String taxIdentificationNumber){
        TaxCalculator.builder()
                .incomingVat(1.23)
                .outgoingVat(1.23)
                .income(10.0)
                .costs(5.0)
                .earnings(2.0)
                .vatToPay(1.0)
                .build();
    }
}
