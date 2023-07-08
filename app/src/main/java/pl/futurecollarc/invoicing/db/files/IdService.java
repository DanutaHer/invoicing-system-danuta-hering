package pl.futurecollarc.invoicing.db.files;

import java.util.List;
import pl.futurecollarc.invoicing.service.FilesService;

public class IdService {

  private final FilesService filesService;
  private int nextId = 1;

  public IdService(FilesService filesService, String path) {
    this.filesService = filesService;

    List<String> lines = filesService.readAllLines(path);
    if (lines.isEmpty()) {
      filesService.writeTextTo(path, "1");
    } else {
      nextId = Integer.parseInt(lines.get(0));
    }
  }

  public int getNextIdAndIncreament(String path) {
    filesService.writeTextTo(path, String.valueOf(nextId + 1));
    return nextId++;
  }
}
