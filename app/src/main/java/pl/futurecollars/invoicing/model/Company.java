package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @ApiModelProperty(value = "Company name", required = true, example = "Global LTD")
    private String name;
    @ApiModelProperty(value = "Company address", required = true, example = "ul. Zlota 12/3")
    private String address;
    @ApiModelProperty(value = "Company tax identification number", required = true, example = "123456789")
    private String taxIdentificationNumber;
}
