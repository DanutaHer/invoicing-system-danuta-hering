package pl.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.bson.Document;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@AllArgsConstructor
public class MongoBasedDatabase implements Database {

    private MongoCollection<Invoice> mongoCollection;
    private final MongoIdProvider idProvider;

    @Override
    public long save(Invoice invoice) {
        invoice.setId(idProvider.getNextIdAndIncrement());
        mongoCollection.insertOne(invoice);
        return invoice.getId();
    }

    @Override
    public Optional<Invoice> getByID(long id) {
        return Optional.ofNullable(mongoCollection.find(idFilter(id)).first());
    }

    @Override
    public List<Invoice> getAll() {
        return StreamSupport
            .stream(mongoCollection.find().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> update(long id, Invoice updatedInvoice) {
        updatedInvoice.setId(id);
        return Optional.ofNullable(mongoCollection.findOneAndReplace(idFilter(id), updatedInvoice));
    }

    @Override
    public Optional<Invoice> delete(long id) {
        return Optional.ofNullable(mongoCollection.findOneAndDelete(idFilter(id)));
    }

    private Document idFilter(long id) {
        return new Document("_id", id);
    }
}
