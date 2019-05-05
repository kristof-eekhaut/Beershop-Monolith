package be.ordina.beershop.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AddProductToShoppingCart {

    @NotNull
    private final String productId;
    @NotNull
    private final int quantity;

    @JsonCreator
    public AddProductToShoppingCart(@JsonProperty("productId") final String productId,
                                    @JsonProperty("quantity") final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
