package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.domain.SingleValueObject;

import java.util.UUID;

public class ShoppingCartId extends SingleValueObject<UUID> implements Identifier {

    private ShoppingCartId(UUID value) {
        super(value);
    }

    public static ShoppingCartId shoppingCartId(UUID value) {
        return new ShoppingCartId(value);
    }

    public static ShoppingCartId fromString(String shoppingCartId) {
        return shoppingCartId(UUID.fromString(shoppingCartId));
    }
}
