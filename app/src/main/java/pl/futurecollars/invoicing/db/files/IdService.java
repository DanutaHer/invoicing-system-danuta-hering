package pl.futurecollars.invoicing.db.files;

import java.nio.file.Path;
import java.util.List;
import pl.futurecollars.invoicing.service.FilesService;

public class IdService {

  private final FilesService filesService;
  private final Path idFilePath;
  private int nextId = 1;

  public IdService(Path idFilePath, FilesService filesService) {
    this.idFilePath = idFilePath;
    this.filesService = filesService;

    List<String> lines = filesService.readAllLines(idFilePath);
    if (lines.isEmpty()) {
      filesService.writeTextTo(idFilePath, "1");
    } else {
      nextId = Integer.parseInt(lines.get(0));
    }
  }

  public int getNextIdAndIncreament() {
    filesService.writeTextTo(idFilePath, String.valueOf(nextId + 1));
    return nextId++;
  }
}