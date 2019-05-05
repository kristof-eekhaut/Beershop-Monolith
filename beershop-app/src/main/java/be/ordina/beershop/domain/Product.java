package be.ordina.beershop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "PRODUCT")
public class Product {

    @Id
    @Column(name = "ID")
    private UUID id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "CREATED_ON")
    @JsonIgnore
    private LocalDateTime createdOn;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts = new ArrayList<>();
    @Column(name = "ALCOHOL_PERCENTAGE")
    private BigDecimal alcoholPercentage;

    @Embedded
    private Weight weight;

    public Product() {
    }

    public Product(UUID id, String name, int quantity, BigDecimal price, BigDecimal alcoholPercentage) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.alcoholPercentage = alcoholPercentage;
    }

    private Product(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setQuantity(builder.quantity);
        setPrice(builder.price);
        setCreatedOn(builder.createdOn);
        discounts = builder.discounts;
        setAlcoholPercentage(builder.alcoholPercentage);
        setWeight(builder.weight);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public int getQuantity() {
        return quantity;
    }

    @JsonProperty
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(final Weight weight) {
        this.weight = weight;
    }

    public void addDiscount(final Discount discount) {
        discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return new ArrayList<>(discounts);
    }

    public BigDecimal getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(final BigDecimal alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    @JsonProperty
    public QuantityIndicator getQuantityIndicator() {
        if(quantity == 0) {
            return QuantityIndicator.SOLD_OUT;
        } else if(quantity < 5) {
            return QuantityIndicator.ALMOST_SOLD_OUT;
        } else {
            return QuantityIndicator.PLENTY_AVAILABLE;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String name;
        private int quantity;
        private BigDecimal price;
        private LocalDateTime createdOn;
        private List<Discount> discounts = new ArrayList<>();
        private BigDecimal alcoholPercentage;
        private Weight weight;

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

        public Builder createdOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder discounts(List<Discount> discounts) {
            this.discounts = discounts;
            return this;
        }

        public Builder alcoholPercentage(BigDecimal alcoholPercentage) {
            this.alcoholPercentage = alcoholPercentage;
            return this;
        }

        public Builder weight(Weight weight) {
            this.weight = weight;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
