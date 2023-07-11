package pl.futurecollarc.invoicing.db;

import java.util.List;
import java.util.Optional;
import pl.futurecollarc.invoicing.model.Invoice;

public interface Database {

  int save(Invoice invoice);

  Optional<Invoice> getByID(int id);

  List<Invoice> getAll();

  Optional<Invoice> update(int id, Invoice updatedInvoice);

  Optional<Invoice> delete(int id);
}
