package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

@AllArgsConstructor
public class JpaDatabase<T extends WithId> implements Database<T> {

    private final CrudRepository<T, Long> itemRepository;

    @Override
    public long save(Invoice invoice) {
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return savedInvoice.getId();
    }

    @Override
    public Optional<T> getByID(long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<T> getAll() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        Optional<T> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
            T item = itemOptional.get();
            itemRepository.save(updatedItem);
        }
        return itemOptional;
    }

    @Override
    public Optional<T> delete(long id) {
        Optional<T> item = itemRepository.findById(id);
        item.ifPresent(itemRepository::delete);
        return item;
    }
}
