package be.ordina.beershop.repository.entities;

import be.ordina.beershop.product.WeightUnit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Embeddable
public class JPAWeight {

    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "UNIT")
    @Enumerated(value = EnumType.STRING)
    private WeightUnit unit;

    protected JPAWeight() {
        // hibernate
    }

    private JPAWeight(BigDecimal amount, WeightUnit unit) {
        setAmount(amount);
        setUnit(unit);
    }

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

    @Deprecated // TODO remove
    public BigDecimal getAmountInGrams() {
        switch (unit) {
            case GRAM: return amount;
            case KILO_GRAM: return amount.multiply(new BigDecimal("1000"));
            default: throw new RuntimeException("Unit unknown");
        }
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    public static JPAWeight weight(BigDecimal amount, WeightUnit unit) {
        return new JPAWeight(amount, unit);
    }
}
