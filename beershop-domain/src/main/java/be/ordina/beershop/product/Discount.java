package be.ordina.beershop.product;

import be.ordina.beershop.domain.AbstractValueObject;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public class Discount extends AbstractValueObject {

    public static final Discount NONE = Discount.builder().percentage(BigDecimal.ZERO).startDate(LocalDate.now()).endDate(LocalDate.now()).build();

    private final BigDecimal percentage;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Discount(Builder builder) {
        this.percentage = requireNonNull(builder.percentage);
        this.startDate = requireNonNull(builder.startDate);
        this.endDate = requireNonNull(builder.endDate);
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BigDecimal percentage;
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder percentage(BigDecimal percentage) {
            this.percentage = percentage;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Discount build() {
            return new Discount(this);
        }
    }
}
