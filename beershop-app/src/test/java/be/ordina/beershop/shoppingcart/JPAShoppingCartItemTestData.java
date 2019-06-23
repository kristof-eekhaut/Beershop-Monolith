package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAShoppingCartItem;

import java.util.UUID;

public class JPAShoppingCartItemTestData {

    public static final int DEFAULT_QUANTITY = 10;

    public static JPAShoppingCartItem.Builder shoppingCartItem(UUID shoppingCartId, JPAProduct product) {
        return JPAShoppingCartItem.builder()
                .id(new JPAShoppingCartItem.JPAShoppingCartItemId(shoppingCartId, product.getId()))
                .quantity(DEFAULT_QUANTITY)
                .productPrice(product.getPrice());
    }
}
