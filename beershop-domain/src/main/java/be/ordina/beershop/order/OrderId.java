package be.ordina.beershop.order;

import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.domain.SingleValueObject;

import java.util.UUID;

public class OrderId extends SingleValueObject<UUID> implements Identifier {

    private OrderId(UUID value) {
        super(value);
    }

    public static OrderId orderId(UUID value) {
        return new OrderId(value);
    }

    public static OrderId fromString(String orderId) {
        return orderId(UUID.fromString(orderId));
    }
}
