package pl.futurecollars.invoicing.db.files;

import pl.futurecollars.invoicing.service.FilesService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class IdService {

    private final FilesService filesService;
    private final Path idFilePath;
    private int nextId = 1;

    public IdService(Path idFilePath, FilesService filesService) {
        this.idFilePath = idFilePath;
        this.filesService = filesService;

        try {
            List<String> lines = filesService.readAllLines(idFilePath);
            if (lines.isEmpty()) {
                filesService.writeTextTo(idFilePath, "1");
            } else {
                nextId = Integer.parseInt(lines.get(0));
            }
        } catch (IOException exception) {
            throw new RuntimeException("Failed to initialize id database", exception);
        }
    }

    public int getNextIdAndIncreament() {
        try {
            filesService.writeTextTo(idFilePath, String.valueOf(nextId + 1));
            return nextId++;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to initialize id database", exception);
        }
    }
}
