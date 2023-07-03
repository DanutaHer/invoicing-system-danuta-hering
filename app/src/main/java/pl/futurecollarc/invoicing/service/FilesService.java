package pl.futurecollarc.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FilesService {

  public void writeInvoicesTo(String pathToFile, List<String> text) {
    try {
      Files.write(Paths.get(pathToFile), text);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public List<String> readInvoicesFrom(String pathToFile) {
    try {
      return Files.readAllLines(Paths.get(pathToFile));
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public void writeTextTo(String pathToFile, String text) {
    try {
      Files.writeString(Paths.get(pathToFile), text);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

//  public String readTextFrom(String pathToFile) {
//    try {
//      return Files.readString(Paths.get(pathToFile));
//    } catch (IOException exception) {
//      throw new RuntimeException(exception);
//    }
//  }
}
