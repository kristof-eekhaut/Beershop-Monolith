package be.ordina.beershop.product;

import be.ordina.beershop.exception.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {

    public static final ProductErrorCode ERROR_CODE = ProductErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(ProductId productId) {
        super(ERROR_CODE.getCode(), "Product not found: " + productId);
    }
}