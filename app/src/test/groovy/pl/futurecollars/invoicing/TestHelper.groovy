package pl.futurecollars.invoicing


import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry

import java.time.LocalDate

import static pl.futurecollars.invoicing.model.Vat.VAT_5

class TestHelper {

    static company(int id) {
        new Company("Name $id", "ul. Green", "123456789");
    }

    static product(int id) {
        new InvoiceEntry("Descr $id", BigDecimal.ONE, BigDecimal.ONE, VAT_5);
    }

    static invoice(int id) {
        new Invoice(id, LocalDate.now(), company(id), company(id), List.of(product(id)));
    }
}
