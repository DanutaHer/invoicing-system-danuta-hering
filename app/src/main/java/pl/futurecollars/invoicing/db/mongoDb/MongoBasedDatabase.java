package pl.futurecollars.invoicing.db.mongoDb;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.bson.Document;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

@AllArgsConstructor
public class MongoBasedDatabase<T extends WithId> implements Database<T> {

    private final MongoIdProvider idProvider;
    private MongoCollection<T> mongoCollection;

    @Override
    public long save(T item) {
        item.setId(idProvider.getNextIdAndIncrement());
        mongoCollection.insertOne(item);
        return item.getId();
    }

    @Override
    public Optional<T> getByID(long id) {
        return Optional.ofNullable(mongoCollection.find(idFilter(id)).first());
    }

    @Override
    public List<T> getAll() {
        return StreamSupport
            .stream(mongoCollection.find().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        updatedItem.setId(id);
        T item = mongoCollection.findOneAndReplace(idFilter(id), updatedItem);
        return getByID(id);
    }

    @Override
    public Optional<T> delete(long id) {
        return Optional.ofNullable(mongoCollection.findOneAndDelete(idFilter(id)));
    }

    private Document idFilter(long id) {
        return new Document("_id", id);
    }
}
