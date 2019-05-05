package be.ordina.beershop.integrationTests.product;

import be.ordina.beershop.integrationTests.WeightDto;

class UpdateProductStockDTO {

    private final String name;
    private final int quantity;
    private final String price;
    private final String alcoholPercentage;
    private final WeightDto weight;

    private UpdateProductStockDTO(Builder builder) {
        name = builder.name;
        quantity = builder.quantity;
        price = builder.price;
        alcoholPercentage = builder.alcoholPercentage;
        weight = builder.weight;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public WeightDto getWeight() {
        return weight;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private int quantity;
        private String price;
        private String alcoholPercentage;
        private WeightDto weight;

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

        public Builder price(String price) {
            this.price = price;
            return this;
        }

        public Builder alcoholPercentage(String alcoholPercentage) {
            this.alcoholPercentage = alcoholPercentage;
            return this;
        }

        public Builder weight(WeightDto weight) {
            this.weight = weight;
            return this;
        }

        public UpdateProductStockDTO build() {
            return new UpdateProductStockDTO(this);
        }
    }
}
