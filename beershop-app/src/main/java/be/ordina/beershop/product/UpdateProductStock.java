package be.ordina.beershop.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class UpdateProductStock {

    @NotNull
    private final int quantity;

    @JsonCreator
    public UpdateProductStock(@JsonProperty("quantity") final int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
