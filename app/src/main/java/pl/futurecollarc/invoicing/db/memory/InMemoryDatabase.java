package pl.futurecollarc.invoicing.db.memory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import pl.futurecollarc.invoicing.db.Database;
import pl.futurecollarc.invoicing.model.Invoice;

@Data
public class InMemoryDatabase implements Database {

  private Map<Integer, Invoice> memoryDatabase = new LinkedHashMap<>();
  private int count = 1;

  @Override
  public int save(Invoice invoice) {
    this.memoryDatabase.put(count, invoice);
    return count++;
  }

  @Override
  public Optional<Invoice> getByID(int id) {
    printIllegalArgumentException(id);
    return memoryDatabase.values()
        .stream()
        .filter(p -> p == memoryDatabase.get(id))
        .findAny();
  }

  @Override
  public List<Invoice> getAll() {
    return memoryDatabase.values()
        .stream()
        .toList();
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    printIllegalArgumentException(id);
    memoryDatabase.replace(id, memoryDatabase.get(id), updatedInvoice);
  }

  @Override
  public void delete(int id) {
    printIllegalArgumentException(id);
    memoryDatabase.remove(id, memoryDatabase.get(id));
  }

  private void printIllegalArgumentException(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("Error: id cannot be negative");
    }
  }
}
