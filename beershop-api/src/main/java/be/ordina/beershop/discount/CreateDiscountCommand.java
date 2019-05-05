package be.ordina.beershop.discount;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateDiscountCommand {

    private final String productId;
    private final BigDecimal percentage;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private CreateDiscountCommand(Builder builder) {
        this.productId = builder.productId;
        this.percentage = builder.percentage;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    public String getProductId() {
        return productId;
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
        private String productId;
        private BigDecimal percentage;
        private LocalDate startDate;
        private LocalDate endDate;

        private Builder() {
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

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

        public CreateDiscountCommand build() {
            return new CreateDiscountCommand(this);
        }
    }
}
