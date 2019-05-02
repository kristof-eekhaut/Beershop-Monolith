package be.ordina.beershop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
public class Weight {

    @Column(name = "AMOUNT")
    @NotNull
    private BigDecimal amount;
    @Column(name = "UNIT")
    @Enumerated(value = EnumType.STRING)
    @NotNull
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

    @JsonIgnore
    public BigDecimal getAmountInGrams() {
        switch (unit) {
            case GRAM: return amount;
            case KILO_GRAM: return amount.multiply(new BigDecimal("1000"));
            default: throw new RuntimeException("Unit unknown");
        }
    }
}
