package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

  @ApiModelProperty(value = "Invoice id", required = true, example = "1")
  private int id;
  @ApiModelProperty(value = "Invoice creation date", required = true, example = "2023-06-15")
  private LocalDate date;
  @ApiModelProperty(value = "Buyer company", required = true)
  private Company buyer;
  @ApiModelProperty(value = "Seller company", required = true)
  private Company seller;
  @ApiModelProperty(value = "List of invoice entries", required = true)
  private List<InvoiceEntry> entries;
}
