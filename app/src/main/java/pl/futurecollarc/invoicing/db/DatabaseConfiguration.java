package pl.futurecollarc.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollarc.invoicing.db.files.FileDatabase;
import pl.futurecollarc.invoicing.db.files.IdService;
import pl.futurecollarc.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollarc.invoicing.service.FilesService;
import pl.futurecollarc.invoicing.service.JsonService;

@Configuration
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
    Path databasePath = Files.createTempFile(DATABASE_LOCATION, INVOICES_FILE_NAME);
    return new FileDatabase(filesService, jsonService, idService, databasePath);
  }

  @Bean
  public Database inMemoryDatabase() {
    return new InMemoryDatabase();
  }
}
