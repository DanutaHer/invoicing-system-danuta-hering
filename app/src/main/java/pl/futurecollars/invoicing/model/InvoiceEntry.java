package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntry {

  @ApiModelProperty(value = "Product description", required = true, example = "Samsung S23 Ultra")
  private String description;

  @ApiModelProperty(value = "Product price", required = true, example = "5640.00")
  private BigDecimal price;

  @ApiModelProperty(value = "Product tax", required = true, example = "564.00")
  private BigDecimal vatValue;

  @ApiModelProperty(value = "Tax rate", required = true, example = "10")
  private Vat vatRate;
}
