package be.ordina.beershop.product;

import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.domain.SingleValueObject;

import java.util.UUID;

public class ProductId extends SingleValueObject<UUID> implements Identifier {

    private ProductId(UUID value) {
        super(value);
    }

    public static ProductId productId(UUID value) {
        return new ProductId(value);
    }

    public static ProductId fromString(String productId) {
        return productId(UUID.fromString(productId));
    }
}
