package be.ordina.beershop.integrationTests.discount;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class CreateDiscountDTO {

    private UUID productId;
    private BigDecimal percentage;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    private CreateDiscountDTO(Builder builder) {
        productId = builder.productId;
        percentage = builder.percentage;
        startDate = builder.startDate;
        endDate = builder.endDate;
    }

    public UUID getProductId() {
        return productId;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID productId;
        private BigDecimal percentage;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;

        private Builder() {
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder percentage(BigDecimal percentage) {
            this.percentage = percentage;
            return this;
        }

        public Builder startDate(ZonedDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(ZonedDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public CreateDiscountDTO build() {
            return new CreateDiscountDTO(this);
        }
    }
}
