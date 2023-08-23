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
    @ApiModelProperty(value = "Income minus costs", required = true, example = "2.0")
    private BigDecimal incomeMinusCosts;
    @ApiModelProperty(value = "Pension insurance", required = true, example = "2.0")
    private BigDecimal pensionInsurance;
    @ApiModelProperty(value = "Income minus costs minus pension insurance", required = true, example = "2.0")
    private BigDecimal incomeMinusCostsMinusPensionInsurance;
    @ApiModelProperty(value = "Income minus costs minus pension insurance rounded", required = true, example = "2.0")
    private BigDecimal incomeMinusCostsMinusPensionInsuranceRounded;
    @ApiModelProperty(value = "Income tax", required = true, example = "2.0")
    private BigDecimal incomeTax;
    @ApiModelProperty(value = "Health insurance paid", required = true, example = "2.0")
    private BigDecimal healthInsurancePaid;
    @ApiModelProperty(value = "Health insurance to subtract", required = true, example = "2.0")
    private BigDecimal healthInsuranceToSubtract;
    @ApiModelProperty(value = "Income tax minus health insurance", required = true, example = "2.0")
    private BigDecimal incomeTaxMinusHealthInsurance;
    @ApiModelProperty(value = "Final income tax", required = true, example = "2.0")
    private BigDecimal finalIncomeTax;
    @ApiModelProperty(value = "Vat to Pay (Incoming VAT - Outgoing VAT)", required = true, example = "1.0")
    private BigDecimal vatToPay;
}
