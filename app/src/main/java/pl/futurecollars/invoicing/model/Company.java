package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    @ApiModelProperty(value = "Company name", required = true, example = "Super Star Sp z o.o.")
    private String name;
    @ApiModelProperty(value = "Company address", required = true, example = "ul. Zielona 10/1A, 00-432 Warszawa, Polska")
    private String address;
    @ApiModelProperty(value = "Company tax identification number", required = true, example = "123-456-789")
    private String taxIdentificationNumber;
    @ApiModelProperty(value = "Pension insurance amount", required = true, example = "514.57")
    private BigDecimal pensionInsurance;
    @ApiModelProperty(value = "Health insurance amount", required = true, example = "319.94")
    private BigDecimal healthInsurance;
}
