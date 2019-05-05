package be.ordina.beershop.discount;

import be.ordina.beershop.exception.EntityNotFoundException;

import java.util.UUID;

public class ProductNotFoundException extends EntityNotFoundException {

    public static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(UUID productId) {
        super(ERROR_CODE, "Product not found: " + productId);
    }
}
