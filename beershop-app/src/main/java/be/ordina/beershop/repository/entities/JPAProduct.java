package be.ordina.beershop.repository.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity(name = "PRODUCT")
public class JPAProduct {

    @Id
    @Column(name = "ID")
    private final UUID id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "CREATED_ON")
    private final LocalDateTime createdOn;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JPADiscount> discounts = new ArrayList<>();
    @Column(name = "ALCOHOL_PERCENTAGE")
    private BigDecimal alcoholPercentage;

    @Embedded
    private JPAWeight weight;

    public JPAProduct() {
        // hibernate
        this.id = null;
        this.createdOn = null;
    }

    private JPAProduct(Builder builder) {
        this.id = builder.id;
        this.createdOn = LocalDateTime.now();

        setName(builder.name);
        setQuantity(builder.quantity);
        setPrice(builder.price);
        setDiscounts(builder.discounts);
        setAlcoholPercentage(builder.alcoholPercentage);
        setWeight(builder.weight);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public JPAWeight getWeight() {
        return weight;
    }

    public void setWeight(final JPAWeight weight) {
        this.weight = weight;
    }

    public List<JPADiscount> getDiscounts() {
        return new ArrayList<>(discounts);
    }

    public void setDiscounts(List<JPADiscount> discounts) {
        this.discounts.clear();
        this.discounts.addAll(discounts);
    }

    public Optional<BigDecimal> getAlcoholPercentage() {
        return Optional.ofNullable(alcoholPercentage);
    }

    public void setAlcoholPercentage(final BigDecimal alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String name;
        private int quantity;
        private BigDecimal price;
        private List<JPADiscount> discounts = new ArrayList<>();
        private BigDecimal alcoholPercentage;
        private JPAWeight weight;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder discounts(List<JPADiscount> discounts) {
            this.discounts = discounts;
            return this;
        }

        public Builder alcoholPercentage(BigDecimal alcoholPercentage) {
            this.alcoholPercentage = alcoholPercentage;
            return this;
        }

        public Builder weight(JPAWeight weight) {
            this.weight = weight;
            return this;
        }

        public JPAProduct build() {
            return new JPAProduct(this);
        }
    }
}
