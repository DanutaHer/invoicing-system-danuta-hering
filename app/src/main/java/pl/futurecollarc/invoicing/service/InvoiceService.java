package pl.futurecollarc.invoicing.service;

import java.util.List;
import java.util.Optional;
import pl.futurecollarc.invoicing.db.Database;
import pl.futurecollarc.invoicing.model.Invoice;

public class InvoiceService {

  private final Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  public void save(Invoice invoice) {
    database.save(invoice);
  }

  public Optional<Invoice> getByID(int id) {
    return database.getByID(id);
  }

  public List<Invoice> getAll() {
    return database.getAll();
  }

  void update(int id, Invoice updatedInvoice) {
    database.update(id, updatedInvoice);
  }

  void delete(int id) {
    database.delete(id);
  }
}
