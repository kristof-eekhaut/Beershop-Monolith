package be.ordina.beershop.product.event;

import be.ordina.beershop.common.Price;
import be.ordina.beershop.domain.AbstractDomainEvent;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.product.Weight;

import java.math.BigDecimal;

public class ProductCreatedEvent extends AbstractDomainEvent {

    private ProductId productId;
    private String name;
    private int quantity;
    private Price price;
    private BigDecimal alcoholPercentage;
    private Weight weight;

    private ProductCreatedEvent(Builder builder) {
        productId = builder.productId;
        name = builder.name;
        quantity = builder.quantity;
        price = builder.price;
        alcoholPercentage = builder.alcoholPercentage;
        weight = builder.weight;
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Price getPrice() {
        return price;
    }

    public BigDecimal getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public Weight getWeight() {
        return weight;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ProductId productId;
        private String name;
        private int quantity;
        private Price price;
        private BigDecimal alcoholPercentage;
        private Weight weight;

        private Builder() {
        }

        public Builder productId(ProductId productId) {
            this.productId = productId;
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

        public Builder price(Price price) {
            this.price = price;
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

        public ProductCreatedEvent build() {
            return new ProductCreatedEvent(this);
        }
    }
}
