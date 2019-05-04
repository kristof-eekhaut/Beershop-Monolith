package be.ordina.beershop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "DISCOUNT")
public class Discount {

    public static final Discount NONE = new Discount(BigDecimal.ZERO, ZonedDateTime.now(), ZonedDateTime.now());

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "PERCENTAGE")
    private BigDecimal percentage;

    @Column(name = "START_DATE")
    private ZonedDateTime startDate;

    @Column(name = "END_DATE")
    private ZonedDateTime endDate;

    @Column(name = "PRODUCT_ID", insertable = false, updatable = false)
    private UUID productId;

    protected Discount() {
        // hibernate
    }

    public Discount(final BigDecimal percentage, final ZonedDateTime startDate, final ZonedDateTime endDate) {
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private Discount(Builder builder) {
        setId(builder.id);
        setPercentage(builder.percentage);
        setStartDate(builder.startDate);
        setEndDate(builder.endDate);
        setProductId(builder.productId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(final BigDecimal percentage) {
        this.percentage = percentage;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(final ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(final ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(final UUID productId) {
        this.productId = productId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private BigDecimal percentage;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private UUID productId;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
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

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Discount build() {
            return new Discount(this);
        }
    }
}
