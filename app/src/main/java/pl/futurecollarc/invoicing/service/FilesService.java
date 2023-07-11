package pl.futurecollarc.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FilesService {

  public void appendLineToFile(String pathToFile, String line) {
    try {
      Files.write(Paths.get(pathToFile), (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void writeLinesToFile(String pathToFile, List<String> lines) {
    try {
      Files.write(Paths.get(pathToFile), lines, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public void writeTextTo(String pathToFile, String line) {
    try {
      Files.write(Paths.get(pathToFile), line.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public List<String> readAllLines(String pathToFile) {
    try {
      return Files.readAllLines(Paths.get(pathToFile));
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }
}
