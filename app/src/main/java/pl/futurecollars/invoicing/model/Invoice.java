package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
