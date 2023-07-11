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

  public int save(Invoice invoice) {
    return database.save(invoice);
  }

  public Optional<Invoice> getByID(int id) {
    return database.getByID(id);
  }

  public List<Invoice> getAll() {
    return database.getAll();
  }

  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    return database.update(id, updatedInvoice);
  }

  public Optional<Invoice> delete(int id) {
    return database.delete(id);
  }
}
