package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.files.FileDatabase;
import pl.futurecollars.invoicing.db.files.IdService;
import pl.futurecollars.invoicing.db.jpa.InvoiceRepository;
import pl.futurecollars.invoicing.db.jpa.JpaDatabase;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.db.sql.SqlDatabase;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.JsonService;

@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Bean
    public IdService idService(FilesService filesService,
                               @Value("${invoicing-system.database.directory}") String databaseLocation,
                               @Value("${invoicing-system.database.file.id}") String idFile) throws IOException {
        Path idPath = Files.createTempFile(databaseLocation, idFile);
        return new IdService(idPath, filesService);
    }

    @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "file")
    @Bean
    public Database fileDatabase(FilesService filesService, JsonService jsonService, IdService idService,
                                 @Value("${invoicing-system.database.directory}") String databaseLocation,
                                 @Value("${invoicing-system.database.file.invoices}") String invoicesFile) throws IOException {

        log.info("Creating in-file database: " + invoicesFile);
        Path databasePath = Files.createTempFile(databaseLocation, invoicesFile);
        return new FileDatabase(filesService, jsonService, idService, databasePath);
    }

    @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "memory")
    @Bean
    public Database inMemoryDatabase() {
        log.info("Creating inMemory database");
        return new InMemoryDatabase();
    }

    @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "sql")
    @Bean
    public Database sqlDatabase(JdbcTemplate jdbcTemplate) {
        log.info("Creating sql database");
        return new SqlDatabase(jdbcTemplate);
    }

    @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "jpa")
    @Bean
    public Database jpaDatabase(InvoiceRepository invoiceRepository) {
        log.info("Creating jpa database");
        return new JpaDatabase(invoiceRepository);
    }
}
