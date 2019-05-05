package be.ordina.beershop.product.dto;

import java.math.BigDecimal;

public class WeightDTO {

    private final BigDecimal amount;
    private final String unit;

    public WeightDTO(final BigDecimal amount,
                     final String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}
