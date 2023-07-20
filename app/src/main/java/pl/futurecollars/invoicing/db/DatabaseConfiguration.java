package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.files.FileDatabase;
import pl.futurecollars.invoicing.db.files.IdService;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.JsonService;

@Configuration
public class DatabaseConfiguration {

  @Bean
  public IdService idService(FilesService filesService,
                             @Value("${invoicing-system.database.directory}") String databaseLocation,
                             @Value("${invoicing-system.database.id.file}") String idFile) throws IOException {
    Path idPath = Files.createTempFile(databaseLocation, idFile);
    return new IdService(idPath, filesService);
  }

  @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "file")
  @Bean
  public Database fileDatabase(FilesService filesService, JsonService jsonService, IdService idService,
                               @Value("${invoicing-system.database.directory}") String databaseLocation,
                               @Value("${invoicing-system.database.invoices.file}") String invoicesFile) throws IOException {
    Path databasePath = Files.createTempFile(databaseLocation, invoicesFile);
    return new FileDatabase(filesService, jsonService, idService, databasePath);
  }

  @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "memory")

  private static final String DATABASE_LOCATION = "db";
  private static final String INVOICES_FILE_NAME = "invoices.txt";
  private static final String ID_FILE_NAME = "id.txt";

  @Bean
  public IdService idService(FilesService filesService) throws IOException {
    Path idPath = Files.createTempFile(DATABASE_LOCATION, ID_FILE_NAME);
    return new IdService(idPath, filesService);
  }

  @Bean
  public Database fileDatabase(FilesService filesService, JsonService jsonService, IdService idService) throws IOException {
    Path databasePath = Files.createTempFile(DATABASE_LOCATION, INVOICES_FILE_NAME);
    return new FileDatabase(filesService, jsonService, idService, databasePath);
  }

  @Bean
  public Database inMemoryDatabase() {
    return new InMemoryDatabase();
  }
}
