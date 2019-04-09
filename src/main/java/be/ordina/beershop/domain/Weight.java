package be.ordina.beershop.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Embeddable
public class Weight {

    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "UNIT")
    @Enumerated(value = EnumType.STRING)
    private WeightUnit unit;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public void setUnit(final WeightUnit unit) {
        this.unit = unit;
    }

    public BigDecimal getAmountInGrams() {
        switch (unit) {
            case GRAM: return amount;
            case KILO_GRAM: return amount.multiply(new BigDecimal("1000"));
            default: throw new RuntimeException("Unit unknown");
        }
    }
}
