package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.ShoppingCart;
import be.ordina.beershop.order.LineItemTestData;

import java.util.UUID;

public class ShoppingCartTestData {

    public static ShoppingCart.Builder emptyCart() {
        return ShoppingCart.builder()
                .id(UUID.randomUUID());
    }

    public static ShoppingCart.Builder cartWithItem(JPAProduct product) {
        return ShoppingCart.builder()
                .id(UUID.randomUUID())
                .lineItem(LineItemTestData.lineItem(product).build());
    }
}
