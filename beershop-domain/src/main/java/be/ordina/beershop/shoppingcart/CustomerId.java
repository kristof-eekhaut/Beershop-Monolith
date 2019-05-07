package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.domain.SingleValueObject;

import java.util.UUID;

public class CustomerId extends SingleValueObject<UUID> implements Identifier {

    private CustomerId(UUID value) {
        super(value);
    }

    public static CustomerId customerId(UUID value) {
        return new CustomerId(value);
    }

    public static CustomerId fromString(String customerId) {
        return customerId(UUID.fromString(customerId));
    }
}
