package pl.futurecollarc.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FilesService {

  public void appendLineToFile(Path path, String line) {
    try {
      Files.write(path, (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void writeLinesToFile(Path path, List<String> lines) {
    try {
      Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public void writeTextTo(Path path, String line) {
    try {
      Files.write(path, line.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public List<String> readAllLines(Path path) {
    try {
      return Files.readAllLines(path);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }
}
