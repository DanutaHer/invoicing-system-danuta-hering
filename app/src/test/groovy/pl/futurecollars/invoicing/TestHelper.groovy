package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry

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
}
