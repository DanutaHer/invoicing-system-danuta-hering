package pl.futurecollars.invoicing.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

public interface Database {

  int save(Invoice invoice);

  Optional<Invoice> getByID(int id);

  List<Invoice> getAll();

  Optional<Invoice> update(int id, Invoice updatedInvoice);

  Optional<Invoice> delete(int id);

  default BigDecimal visit(Predicate<Invoice> predicateInvoice, Function<InvoiceEntry, BigDecimal> invoiceEntryToAmount) {
    return getAll()
            .stream()
            .filter(predicateInvoice)
            .flatMap(invoice -> invoice.getEntries().stream())
            .map(invoiceEntryToAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
