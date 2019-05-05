package be.ordina.beershop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity(name = "DISCOUNT")
public class Discount {

    public static final Discount NONE = Discount.builder().percentage(BigDecimal.ZERO).startDate(LocalDate.now()).endDate(LocalDate.now()).build();

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "PERCENTAGE")
    private BigDecimal percentage;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    protected Discount() {
        // hibernate
    }

    private Discount(Builder builder) {
        setId(builder.id);
        setPercentage(builder.percentage);
        setStartDate(builder.startDate);
        setEndDate(builder.endDate);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private BigDecimal percentage;
        private LocalDate startDate;
        private LocalDate endDate;

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
