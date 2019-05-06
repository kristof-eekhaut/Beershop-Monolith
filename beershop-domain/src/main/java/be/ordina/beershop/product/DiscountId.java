package be.ordina.beershop.product;

import be.ordina.beershop.domain.AbstractIdentifier;

import java.util.UUID;

class DiscountId extends AbstractIdentifier<UUID> {

    private DiscountId(UUID value) {
        super(value);
    }

    static DiscountId discountId(UUID value) {
        return new DiscountId(value);
    }

    public static DiscountId fromString(String discountId) {
        return discountId(UUID.fromString(discountId));
    }
}
