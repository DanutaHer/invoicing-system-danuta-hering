package pl.futurecollarc.invoicing.db.files;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.futurecollarc.invoicing.config.Config;
import pl.futurecollarc.invoicing.db.Database;
import pl.futurecollarc.invoicing.model.Invoice;
import pl.futurecollarc.invoicing.service.FilesService;
import pl.futurecollarc.invoicing.service.JsonService;

@Data
@AllArgsConstructor
public class FileDatabase implements Database {

  private final List<String> allInvoices = new ArrayList<>();
  private FilesService filesService;
  private JsonService jsonService;

  @Override
  public void save(Invoice invoice) {
    allInvoices.add(jsonService.objectToJson(invoice));
    filesService.writeInvoicesTo(Config.DATABASE_LOCATION, allInvoices);
    filesService.writeTextTo(Config.ID_FILE_LOCATION, String.valueOf(allInvoices.size()));
  }

  @Override
  public Optional<Invoice> getByID(int id) {
    printIllegalArgumentException(id);
    return filesService.readInvoicesFrom(Config.DATABASE_LOCATION).stream()
        .filter(p -> p.equals(allInvoices.get(id)))
        .map(p -> jsonService.jsonToObject(p))
        .findAny();
  }

  @Override
  public List<Invoice> getAll() {
    return filesService.readInvoicesFrom(Config.DATABASE_LOCATION).stream()
        .map(p -> jsonService.jsonToObject(p))
        .toList();
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    printIllegalArgumentException(id);
    List<String> invoicesToUpdate = filesService.readInvoicesFrom(Config.DATABASE_LOCATION);

    invoicesToUpdate.set(id, jsonService.objectToJson(updatedInvoice));
    filesService.writeInvoicesTo(Config.DATABASE_LOCATION, invoicesToUpdate);
  }

  @Override
  public void delete(int id) {
    printIllegalArgumentException(id);
    List<String> updatedInvoices = filesService.readInvoicesFrom(Config.DATABASE_LOCATION);

    updatedInvoices.remove(id);
    filesService.writeInvoicesTo(Config.DATABASE_LOCATION, updatedInvoices);
  }

  private void printIllegalArgumentException(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("Error: id cannot be negative");
    }
  }
}
