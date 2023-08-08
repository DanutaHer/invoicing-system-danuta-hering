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
public class InvoiceEntry {

  @ApiModelProperty(value = "Service description", required = true, example = "Execution for the service")
  private String description;
  @ApiModelProperty(value = "Service price", required = true, example = "1500,00")
  private BigDecimal price;
  @ApiModelProperty(value = "Service value", required = true, example = "315,00")
  private BigDecimal vatValue;
  @ApiModelProperty(value = "Vat rate for the service", required = true, example = "VAT_21")
  private Vat vatRate;
}
