package pl.futurecollars.invoicing.model;

import static javax.persistence.CascadeType.ALL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceEntry {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Service id", required = true, example = "1")
    private long id;
    @ApiModelProperty(value = "Service description", required = true, example = "Execution for the service")
    private String description;
    @ApiModelProperty(value = "Service price", required = true, example = "1500.00")
    private BigDecimal netPrice;
    @ApiModelProperty(value = "Service value", required = true, example = "315.00")
    private BigDecimal vatValue;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Vat rate for the service", required = true, example = "VAT_21")
    private Vat vatRate;
    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "expense_related_to_car")
    @ApiModelProperty(value = "Expense related to car", required = true, example = "100.00")
    private Car expenseRelatedToCar;
}
