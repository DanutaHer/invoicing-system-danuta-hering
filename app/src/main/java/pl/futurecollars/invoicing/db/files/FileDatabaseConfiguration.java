package pl.futurecollars.invoicing.db.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.JsonService;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "file")
public class FileDatabaseConfiguration {

    @Bean
    public IdService invoiceIdService(FilesService filesService,
                                      @Value("${invoicing-system.database.directory}") String databaseLocation,
                                      @Value("${invoicing-system.database.file.invoiceId}") String idFile) throws IOException {
        Path idPath = Files.createTempFile(databaseLocation, idFile);
        return new IdService(idPath, filesService);
    }

    @Bean
    public Database<Invoice> invoiceFileDatabase(FilesService filesService, JsonService jsonService, IdService invoiceIdService,
                                                 @Value("${invoicing-system.database.directory}") String databaseLocation,
                                                 @Value("${invoicing-system.database.file.invoices}") String invoicesFile) throws IOException {

        log.info("Creating in-file database: " + invoicesFile);
        Path databasePath = Files.createTempFile(databaseLocation, invoicesFile);
        return new FileDatabase<Invoice>(filesService, jsonService, invoiceIdService, databasePath, Invoice.class);
    }

    @Bean
    public IdService companyIdService(FilesService filesService,
                                      @Value("${invoicing-system.database.directory}") String databaseLocation,
                                      @Value("${invoicing-system.database.file.companyId}") String idFile) throws IOException {
        Path idPath = Files.createTempFile(databaseLocation, idFile);
        return new IdService(idPath, filesService);
    }

    @Bean
    public Database<Company> companyFileDatabase(FilesService filesService, JsonService jsonService, IdService companyIdService,
                                                 @Value("${invoicing-system.database.directory}") String databaseLocation,
                                                 @Value("${invoicing-system.database.file.companies}") String companiesFile) throws IOException {

        log.info("Creating in-file database: " + companiesFile);
        Path databasePath = Files.createTempFile(databaseLocation, companiesFile);
        return new FileDatabase<Company>(filesService, jsonService, companyIdService, databasePath, Company.class);
    }
}
