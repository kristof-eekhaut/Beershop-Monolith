package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.domain.ShoppingCart;
import be.ordina.beershop.order.LineItemTestData;

import java.util.UUID;

public class ShoppingCartTestData {

    public static ShoppingCart.Builder emptyCart() {
        return ShoppingCart.builder()
                .id(UUID.randomUUID());
    }

    public static ShoppingCart.Builder cartWithItem(Product product) {
        return ShoppingCart.builder()
                .id(UUID.randomUUID())
                .lineItem(LineItemTestData.lineItem(product).build());
    }
}
