package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Company id", required = true, example = "1")
    private long id;
    @ApiModelProperty(value = "Company name", required = true, example = "Super Star Sp z o.o.")
    private String name;
    @ApiModelProperty(value = "Company address", required = true, example = "ul. Zielona 10/1A, 00-432 Warszawa, Polska")
    private String address;
    @ApiModelProperty(value = "Company tax identification number", required = true, example = "123-456-789")
    private String taxIdentificationNumber;
    @ApiModelProperty(value = "Pension insurance amount", required = true, example = "1328.75")
    private BigDecimal pensionInsurance;
    @ApiModelProperty(value = "Health insurance amount", required = true, example = "458.34")
    private BigDecimal healthInsurance;
}
