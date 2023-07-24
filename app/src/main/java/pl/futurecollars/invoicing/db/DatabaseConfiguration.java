package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.files.FileDatabase;
import pl.futurecollars.invoicing.db.files.IdService;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.JsonService;

@Configuration
@Slf4j
public class DatabaseConfiguration {

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
    log.info("Creating in-file database: " + INVOICES_FILE_NAME);
    Path databasePath = Files.createTempFile(DATABASE_LOCATION, INVOICES_FILE_NAME);
    return new FileDatabase(filesService, jsonService, idService, databasePath);
  }

  @Bean
  public Database inMemoryDatabase() {
    log.info("Creating inMemory database");
    return new InMemoryDatabase();
  }
}
