package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAShoppingCart;

import java.util.Arrays;
import java.util.UUID;

public class JPAShoppingCartTestData {

    public static JPAShoppingCart.Builder emptyCart(UUID customerId) {
        return JPAShoppingCart.builder()
                .id(UUID.randomUUID())
                .customerId(customerId);
    }

    public static JPAShoppingCart.Builder cartWithItems(UUID customerId, JPAProduct... products) {
        UUID shoppingCartId = UUID.randomUUID();
        JPAShoppingCart.Builder builder = emptyCart(customerId)
                .id(shoppingCartId);

        Arrays.stream(products)
                .forEach(product -> builder.item(JPAShoppingCartItemTestData.shoppingCartItem(shoppingCartId, product).build()));

        return builder;
    }
}
