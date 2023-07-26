package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

  @ApiModelProperty(value = "Company address", required = true, example = "ul. Owocowa 23, 56-300 Gruszeczka ")
  private String address;

  @ApiModelProperty(value = "Company name", required = true, example = "Owocpol Ltd.")
  private String name;

  @ApiModelProperty(value = "Tax number", required = true, example = "565-897-25-26")
  private String taxIdentificationNumber;
}
