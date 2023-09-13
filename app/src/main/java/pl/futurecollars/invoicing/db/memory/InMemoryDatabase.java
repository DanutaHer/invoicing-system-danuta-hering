package pl.futurecollars.invoicing.db.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

@Data
public class InMemoryDatabase<T extends WithId> implements Database<T> {

    private Map<Long, T> memoryDatabase = new HashMap<>();
    private long count = 1;

    @Override
    public long save(T item) {
        this.memoryDatabase.put(count, item);
        item.setId(count);
        return count++;
    }

    @Override
    public Optional<T> getByID(long id) {
        printIllegalArgumentException(id);
        return memoryDatabase.values()
            .stream()
            .filter(item -> item == memoryDatabase.get(id))
            .findAny();
    }

    @Override
    public List<T> getAll() {
        return memoryDatabase.values()
            .stream()
            .toList();
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        printIllegalArgumentException(id);
        if (memoryDatabase.containsKey(id)) {
            memoryDatabase.put(id, updatedItem);
            updatedItem.setId(id);
        } else {
            return Optional.empty();
        }
        return Optional.of(updatedItem);
    }

    @Override
    public Optional<T> delete(long id) {
        printIllegalArgumentException(id);
        return Optional.ofNullable(memoryDatabase.remove(id));
    }

    private void printIllegalArgumentException(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Error: id cannot be negative");
        }
    }
}
