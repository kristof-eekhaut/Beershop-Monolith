package be.ordina.beershop.discount;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class CreateDiscount {

    @NotNull
    private UUID productId;
    @NotNull
    private BigDecimal percentage;
    @NotNull
    private ZonedDateTime startDate;
    @NotNull
    private ZonedDateTime endDate;

    @JsonCreator
    CreateDiscount(@JsonProperty("productId") final UUID productId,
                   @JsonProperty("percentage") final BigDecimal percentage,
                   @JsonProperty("startDate") final ZonedDateTime startDate,
                  @JsonProperty("endDate") final ZonedDateTime endDate) {
        this.productId = productId;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
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

        public CreateDiscount build() {
            return new CreateDiscount(productId, percentage, startDate, endDate);
        }
    }
}
