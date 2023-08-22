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
public class TaxCalculator {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "TaxCalculator id", required = true, example = "1")
    private long id;
    @ApiModelProperty(value = "Incoming Vat", required = true, example = "1.23")
    private BigDecimal incomingVat;
    @ApiModelProperty(value = "Outgoing Vat", required = true, example = "1.23")
    private BigDecimal outgoingVat;
    @ApiModelProperty(value = "Income", required = true, example = "10.0")
    private BigDecimal income;
    @ApiModelProperty(value = "Costs Vat", required = true, example = "5.0")
    private BigDecimal costs;
    @ApiModelProperty(value = "Earnings (Income - Costs)", required = true, example = "2.0")
    private BigDecimal earnings;
    @ApiModelProperty(value = "Vat to Pay (Incoming VAT - Outgoing VAT)", required = true, example = "1.0")
    private BigDecimal vatToPay;
}
