package pl.futurecollars.invoicing.db.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "mongo")
public class MongoDatabaseConfiguration {

    @Bean
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
    public MongoIdProvider mongoIdProvider(
        @Value("${invoicing-system.database.counter.collection}") String collectionName,
        MongoDatabase mongoDb) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
        return new MongoIdProvider(collection);
    }

    @Bean
    public Database<Invoice> invoiceMongoDatabase(
        @Value("${invoicing-system.database.collection.invoices}") String collectionName,
        MongoDatabase mongoDb,
        MongoIdProvider mongoIdProvider) {
        MongoCollection<Invoice> mongoCollection = mongoDb.getCollection(collectionName, Invoice.class);
        log.info("Creating mongo database for invoices");
        return new MongoBasedDatabase<>(mongoIdProvider, mongoCollection);
    }

    @Bean
    public Database<Company> companyMongoDatabase(
        @Value("${invoicing-system.database.collection.companies}") String collectionName,
        MongoDatabase mongoDb,
        MongoIdProvider mongoIdProvider) {
        MongoCollection<Company> mongoCollection = mongoDb.getCollection(collectionName, Company.class);
        log.info("Creating mongo database for companies");
        return new MongoBasedDatabase<>(mongoIdProvider, mongoCollection);
    }
}
