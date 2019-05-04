package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.exception.BusinessException;

import java.util.UUID;

public class ProductNotFoundException extends BusinessException {

    public static final ShoppingCartErrorCode ERROR_CODE = ShoppingCartErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(UUID productId) {
        super(ERROR_CODE, "Product not found: " + productId);
    }
}
