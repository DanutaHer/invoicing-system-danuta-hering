package pl.futurecollars.invoicing.db;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.mongoDb.MongoBasedDatabase;
import pl.futurecollars.invoicing.db.mongoDb.MongoIdProvider;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
public class MongoDatabaseConfiguration {

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "mongo")
    public MongoDatabase mongoDb(@Value("${invoicing-system.database.name}") String databaseName) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();

        MongoClient client = MongoClients.create(settings);
        return client.getDatabase(databaseName);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "mongo")
    public MongoIdProvider mongoIdProvider(
        @Value("${invoicing-system.database.counter.collection}") String collectionName,
        MongoDatabase mongoDb) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
        return new MongoIdProvider(collection);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "mongo")
    public Database mongoDatabase(
        @Value("${invoicing-system.database.collection}") String collectionName,
        MongoDatabase mongoDb,
        MongoIdProvider mongoIdProvider) {
        MongoCollection<Invoice> mongoCollection = mongoDb.getCollection(collectionName, Invoice.class);
        return new MongoBasedDatabase(mongoCollection, mongoIdProvider);
    }
}
