package be.ordina.beershop.discount;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDiscount {

    @NotNull
    private final String productId;
    @NotNull
    private final BigDecimal percentage;
    @NotNull
    private final LocalDate startDate;
    @NotNull
    private final LocalDate endDate;

    @JsonCreator
    CreateDiscount(@JsonProperty("productId") final String productId,
                   @JsonProperty("percentage") final BigDecimal percentage,
                   @JsonProperty("startDate") final LocalDate startDate,
                   @JsonProperty("endDate") final LocalDate endDate) {
        this.productId = productId;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
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

        public CreateDiscount build() {
            return new CreateDiscount(productId, percentage, startDate, endDate);
        }
    }
}
