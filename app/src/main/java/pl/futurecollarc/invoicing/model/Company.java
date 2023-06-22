package pl.futurecollarc.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {

  private String name;
  private String address;
  private String taxIdentificationNumber;
}
