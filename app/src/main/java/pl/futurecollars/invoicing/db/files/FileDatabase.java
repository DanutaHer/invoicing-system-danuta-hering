package pl.futurecollars.invoicing.db.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.JsonService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
public class FileDatabase implements Database {

    private final FilesService filesService;
    private final JsonService jsonService;
    private final IdService idService;
    private final Path path;

    @Override
    public int save(Invoice invoice) {
        try {
            invoice.setId(idService.getNextIdAndIncreament());
            filesService.appendLineToFile(path, jsonService.objectToJson(invoice));
            return invoice.getId();
        } catch (IOException exception) {
            throw new RuntimeException("Database failed to save invoice", exception);
        }
    }

    @Override
    public Optional<Invoice> getByID(int id) {
        try {
            printIllegalArgumentException(id);
            return filesService.readAllLines(path).stream()
                    .filter(line -> line.contains("\"id\":" + id + ","))
                    .map(jsonInvoice -> jsonService.jsonToObject(jsonInvoice, Invoice.class))
                    .findFirst();
        } catch (IOException exception) {
            throw new RuntimeException("Database failed to get invoice with id: " + id, exception);
        }
    }

    @Override
    public List<Invoice> getAll() {
        try {
            return filesService.readAllLines(path).stream()
                    .map(jsonInvoice -> jsonService.jsonToObject(jsonInvoice, Invoice.class))
                    .toList();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read invoices from file", exception);
        }
    }

    @Override
    public Optional<Invoice> update(int id, Invoice updatedInvoice) {
        try {
            printIllegalArgumentException(id);
            List<String> allInvoices = filesService.readAllLines(path);
            List<String> invoicesToUpdate = new ArrayList<>(allInvoices.stream()
                    .filter(line -> !line.contains("\"id\":" + id + ","))
                    .toList());

            if (allInvoices.size() == invoicesToUpdate.size()) {
                throw new IllegalArgumentException("Id " + id + " does not exist");
            }

            updatedInvoice.setId(id);
            invoicesToUpdate.add(jsonService.objectToJson(updatedInvoice));
            filesService.writeLinesToFile(path, invoicesToUpdate);
            return Optional.of(updatedInvoice);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to update invoice with id: " + id, exception);
        }
    }

    @Override
    public Optional<Invoice> delete(int id) {
        try {
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
        } catch (IOException exception) {
            throw new RuntimeException("Failed to delete invoice with id: " + id, exception);
        }

    }

    private void printIllegalArgumentException(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Error: id cannot be negative");
        }
    }
}
