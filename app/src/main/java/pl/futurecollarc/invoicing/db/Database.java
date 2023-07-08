package pl.futurecollarc.invoicing.db;

import java.util.List;
import java.util.Optional;
import pl.futurecollarc.invoicing.model.Invoice;

public interface Database {

  int save(Invoice invoice);

  Optional<Invoice> getByID(int id);

  List<Invoice> getAll();

  void update(int id, Invoice updatedInvoice);

  void delete(int id);
}
