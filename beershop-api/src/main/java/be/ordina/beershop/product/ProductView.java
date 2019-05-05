package be.ordina.beershop.product;

import be.ordina.beershop.product.dto.WeightDTO;

import java.math.BigDecimal;

public class ProductView {

    private String id;
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal alcoholPercentage;
    private WeightDTO weight;

    private ProductView(Builder builder) {
        id = builder.id;
        name = builder.name;
        quantity = builder.quantity;
        price = builder.price;
        alcoholPercentage = builder.alcoholPercentage;
        weight = builder.weight;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public WeightDTO getWeight() {
        return weight;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private int quantity;
        private BigDecimal price;
        private BigDecimal alcoholPercentage;
        private WeightDTO weight;

        private Builder() {
        }

        public Builder id(String id) {
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

        public Builder alcoholPercentage(BigDecimal alcoholPercentage) {
            this.alcoholPercentage = alcoholPercentage;
            return this;
        }

        public Builder weight(WeightDTO weight) {
            this.weight = weight;
            return this;
        }

        public ProductView build() {
            return new ProductView(this);
        }
    }
}
