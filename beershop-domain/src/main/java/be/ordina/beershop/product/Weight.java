package be.ordina.beershop.product;

import be.ordina.beershop.domain.AbstractValueObject;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public class Weight extends AbstractValueObject {

    private final BigDecimal amount;
    private final WeightUnit unit;

    private Weight(BigDecimal amount,
                   WeightUnit unit) {
        this.amount = requireNonNull(amount);
        this.unit = requireNonNull(unit);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    public BigDecimal getAmountInGrams() {
        switch (unit) {
            case GRAM: return amount;
            case KILO_GRAM: return amount.multiply(new BigDecimal("1000"));
            default: throw new RuntimeException("Unit unknown");
        }
    }

    public static Weight weight(BigDecimal amount, WeightUnit unit) {
        return new Weight(amount, unit);
    }
}
