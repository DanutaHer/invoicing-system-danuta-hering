package pl.futurecollars.invoicing.db.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Data
public class InMemoryDatabase implements Database {

  private Map<Integer, Invoice> memoryDatabase = new HashMap<>();
  private int count = 1;

  @Override
  public int save(Invoice invoice) {
    this.memoryDatabase.put(count, invoice);
    invoice.setId(count);
    return count++;
  }

  @Override
  public Optional<Invoice> getByID(int id) {
    printIllegalArgumentException(id);
    return memoryDatabase.values()
            .stream()
            .filter(invoice -> invoice == memoryDatabase.get(id))
            .findAny();
  }

  @Override
  public List<Invoice> getAll() {
    return memoryDatabase.values()
            .stream()
            .toList();
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    printIllegalArgumentException(id);
    memoryDatabase.replace(id, memoryDatabase.get(id), updatedInvoice);
    updatedInvoice.setId(id);
    return Optional.of(updatedInvoice);
  }

  @Override
  public Optional<Invoice> delete(int id) {
    printIllegalArgumentException(id);
    return Optional.ofNullable(memoryDatabase.remove(id));
  }

  private void printIllegalArgumentException(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("Error: id cannot be negative");
    }
  }
}
