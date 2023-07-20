package pl.futurecollars.invoicing.db.files;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.JsonService;

@Data
@AllArgsConstructor
public class FileDatabase implements Database {

  private final FilesService filesService;
  private final JsonService jsonService;
  private final IdService idService;
  private final Path path;

  @Override
  public int save(Invoice invoice) {
    invoice.setId(idService.getNextIdAndIncreament());
    filesService.appendLineToFile(path, jsonService.objectToJson(invoice));
    return invoice.getId();
  }

  @Override
  public Optional<Invoice> getByID(int id) {
    printIllegalArgumentException(id);
    return filesService.readAllLines(path).stream()
        .filter(line -> line.contains("\"id\":" + id + ","))
        .map(jsonInvoice -> jsonService.jsonToObject(jsonInvoice, Invoice.class))
        .findFirst();
  }

  @Override
  public List<Invoice> getAll() {
    return filesService.readAllLines(path).stream()
        .map(jsonInvoice -> jsonService.jsonToObject(jsonInvoice, Invoice.class))
        .toList();
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    printIllegalArgumentException(id);
    List<String> invoicesToUpdate = new ArrayList<>(filesService.readAllLines(path).stream()
        .filter(line -> !line.contains("\"id\":" + id + ","))
        .toList());

    updatedInvoice.setId(id);
    invoicesToUpdate.add(jsonService.objectToJson(updatedInvoice));
    filesService.writeLinesToFile(path, invoicesToUpdate);
    return Optional.of(updatedInvoice);
  }

  @Override
  public Optional<Invoice> delete(int id) {
    printIllegalArgumentException(id);
    List<String> invoicesToUpdate = new ArrayList<>(filesService.readAllLines(path).stream()
        .filter(line -> !line.contains("\"id\":" + id + ","))
        .toList());

    Optional<Invoice> deletedInvoice = filesService.readAllLines(path).stream()
        .filter(line -> line.contains("\"id\":" + id + ","))
        .map(jsonInvoice -> jsonService.jsonToObject(jsonInvoice, Invoice.class))
        .findAny();

    filesService.writeLinesToFile(path, invoicesToUpdate);
    return deletedInvoice;
  }

  private void printIllegalArgumentException(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("Error: id cannot be negative");
    }
  }
}