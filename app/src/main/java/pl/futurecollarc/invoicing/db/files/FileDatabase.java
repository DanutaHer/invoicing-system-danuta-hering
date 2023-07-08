package pl.futurecollarc.invoicing.db.files;

import static pl.futurecollarc.invoicing.config.Config.DATABASE_LOCATION;
import static pl.futurecollarc.invoicing.config.Config.ID_FILE_LOCATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.futurecollarc.invoicing.db.Database;
import pl.futurecollarc.invoicing.model.Invoice;
import pl.futurecollarc.invoicing.service.FilesService;
import pl.futurecollarc.invoicing.service.JsonService;

@Data
@AllArgsConstructor
public class FileDatabase implements Database {

  private final FilesService filesService;
  private final JsonService jsonService;
  private final IdService idService;

  @Override
  public int save(Invoice invoice) {
    invoice.setId(idService.getNextIdAndIncreament(ID_FILE_LOCATION));
    filesService.appendLineToFile(DATABASE_LOCATION, jsonService.objectToJson(invoice));
    return invoice.getId();
  }

  @Override
  public Optional<Invoice> getByID(int id) {
    printIllegalArgumentException(id);
    printOutOfBoundsException(id);
    return filesService.readAllLines(DATABASE_LOCATION).stream()
        .filter(line -> line.contains("\"id\":" + id + ","))
        .map(jsonService::jsonToObject)
        .findFirst();
  }

  @Override
  public List<Invoice> getAll() {
    return filesService.readAllLines(DATABASE_LOCATION).stream()
        .map(jsonService::jsonToObject)
        .toList();
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    printIllegalArgumentException(id);
    printOutOfBoundsException(id);
    List<String> invoicesToUpdate = new ArrayList<>(filesService.readAllLines(DATABASE_LOCATION).stream()
        .filter(line -> !line.contains("\"id\":" + id + ","))
        .toList());

    updatedInvoice.setId(id);
    invoicesToUpdate.add(jsonService.objectToJson(updatedInvoice));
    filesService.writeLinesToFile(DATABASE_LOCATION, invoicesToUpdate);
  }

  @Override
  public void delete(int id) {
    printIllegalArgumentException(id);
    printOutOfBoundsException(id);
    List<String> invoicesToUpdate = new ArrayList<>(filesService.readAllLines(DATABASE_LOCATION).stream()
        .filter(line -> !line.contains("\"id\":" + id + ","))
        .toList());

    filesService.writeLinesToFile(DATABASE_LOCATION, invoicesToUpdate);
  }

  private void printIllegalArgumentException(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("Error: id cannot be negative");
    }
  }

  private void printOutOfBoundsException(int id) {
    if (id > filesService.readAllLines(DATABASE_LOCATION).size()) {
      throw new IndexOutOfBoundsException("Error: id doesn't exist");
    }
  }
}
