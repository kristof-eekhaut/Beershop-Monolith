package be.ordina.beershop.discount;

import be.ordina.beershop.exception.EntityNotFoundException;
import be.ordina.beershop.product.ProductId;

public class ProductNotFoundException extends EntityNotFoundException {

    public static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(ProductId productId) {
        super(ERROR_CODE.getCode(), "Product not found: " + productId);
    }
}
