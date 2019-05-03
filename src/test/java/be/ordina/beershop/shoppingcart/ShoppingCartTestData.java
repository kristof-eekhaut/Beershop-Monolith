package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.domain.ShoppingCart;

import java.util.UUID;

public class ShoppingCartTestData {

    public static ShoppingCart.Builder emptyCart() {
        return ShoppingCart.builder()
                .id(UUID.randomUUID());
    }
}
