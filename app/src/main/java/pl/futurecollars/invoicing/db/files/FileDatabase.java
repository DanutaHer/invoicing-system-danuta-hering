package pl.futurecollars.invoicing.db.files;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.JsonService;

@Data
@AllArgsConstructor
public class FileDatabase<T extends WithId> implements Database<T> {

    private final FilesService filesService;
    private final JsonService jsonService;
    private final IdService idService;
    private final Path path;
    private final Class<T> clazz;

    @Override
    public long save(T item) {
        try {
            item.setId(idService.getNextIdAndIncrement());
            filesService.appendLineToFile(path, jsonService.objectToJson(item));
            return item.getId();
        } catch (IOException exception) {
            throw new RuntimeException("Database failed to save item", exception);
        }
    }

    @Override
    public Optional<T> getByID(long id) {
        try {
            printIllegalArgumentException(id);
            return filesService.readAllLines(path).stream()
                .filter(line -> line.contains("\"id\":" + id + ","))
                .map(jsonItem -> jsonService.jsonToObject(jsonItem, clazz))
                .findFirst();
        } catch (IOException exception) {
            throw new RuntimeException("Database failed to get item with id: " + id, exception);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return filesService.readAllLines(path).stream()
                .map(jsonItem -> jsonService.jsonToObject(jsonItem, clazz))
                .toList();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read item from file", exception);
        }
    }

    @Override
    public Optional<T> update(long id, T item) {
        try {
            printIllegalArgumentException(id);
            List<String> allItems = filesService.readAllLines(path);
            List<String> itemsToUpdate = new ArrayList<>(allItems.stream()
                .filter(line -> !line.contains("\"id\":" + id + ","))
                .toList());

            if (allItems.size() == itemsToUpdate.size()) {
                throw new IllegalArgumentException("Id " + id + " does not exist");
            }

            item.setId(id);
            itemsToUpdate.add(jsonService.objectToJson(item));
            filesService.writeLinesToFile(path, itemsToUpdate);
            return Optional.of(item);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to update item with id: " + id, exception);
        }
    }

    @Override
    public Optional<T> delete(long id) {
        try {
            printIllegalArgumentException(id);
            List<String> itemsToUpdate = new ArrayList<>(filesService.readAllLines(path).stream()
                .filter(line -> !line.contains("\"id\":" + id + ","))
                .toList());

            Optional<T> deletedItem = filesService.readAllLines(path).stream()
                .filter(line -> line.contains("\"id\":" + id + ","))
                .map(jsonItem -> jsonService.jsonToObject(jsonItem, clazz))
                .findAny();

            filesService.writeLinesToFile(path, itemsToUpdate);
            return deletedItem;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to delete item with id: " + id, exception);
        }

    }

    private void printIllegalArgumentException(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Error: id cannot be negative");
        }
    }
}
