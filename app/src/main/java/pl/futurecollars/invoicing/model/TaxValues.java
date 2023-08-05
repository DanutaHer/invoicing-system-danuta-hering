package pl.futurecollars.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class TaxValues {

    private final BigDecimal incomingVat;
    private final BigDecimal outgoingVat;
    private final BigDecimal income;
    private final BigDecimal costs;
    private final BigDecimal earnings;
    private final BigDecimal vatToPay;
}
