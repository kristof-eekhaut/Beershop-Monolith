package be.ordina.beershop.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class AddProductToShoppingCart {

    @NotNull
    private final UUID productId;
    @NotNull
    private final int quantity;

    @JsonCreator
    public AddProductToShoppingCart(@JsonProperty("productId") final UUID productId,
                                    @JsonProperty("quantity") final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
