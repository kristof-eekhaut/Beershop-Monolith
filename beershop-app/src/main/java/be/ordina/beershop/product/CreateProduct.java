package be.ordina.beershop.product;

import be.ordina.beershop.domain.Weight;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateProduct {

    @NotNull
    private final String name;
    @NotNull
    private final int quantity;
    @NotNull
    @DecimalMin(value = "0.01")
    private final BigDecimal price;
    @NotNull
    private final BigDecimal alcoholPercentage;
    @NotNull
    @Valid
    private final Weight weight;

    @JsonCreator
    CreateProduct(@JsonProperty("customerId") final String name,
                  @JsonProperty("quantity") final int quantity,
                  @JsonProperty("price") final BigDecimal price,
                  @JsonProperty("alcoholPercentage") final BigDecimal alcoholPercentage,
                  @JsonProperty("weight") final Weight weight) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.alcoholPercentage = alcoholPercentage;
        this.weight = weight;
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

    public Weight getWeight() {
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
        private Weight weight;

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

        public Builder weight(Weight weight) {
            this.weight = weight;
            return this;
        }

        public CreateProduct build() {
            return new CreateProduct(
                    name,
                    quantity,
                    price,
                    alcoholPercentage,
                    weight
            );
        }
    }
}
