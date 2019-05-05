package be.ordina.beershop.product;

import be.ordina.beershop.product.dto.WeightDTO;

import java.math.BigDecimal;

public class CreateProductCommand {

    private final String name;
    private final int quantity;
    private final BigDecimal price;
    private final BigDecimal alcoholPercentage;
    private final WeightDTO weight;

    CreateProductCommand(Builder builder) {
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.alcoholPercentage = builder.alcoholPercentage;
        this.weight = builder.weight;
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
        private String name;
        private int quantity;
        private BigDecimal price;
        private BigDecimal alcoholPercentage;
        private WeightDTO weight;

        private Builder() {
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

        public CreateProductCommand build() {
            return new CreateProductCommand(this);
        }
    }
}
