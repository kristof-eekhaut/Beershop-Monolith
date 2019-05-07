package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.exception.BusinessException;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.shoppingcart.exception.ShoppingCartErrorCode;

public class ProductNotFoundException extends BusinessException {

    public static final ShoppingCartErrorCode ERROR_CODE = ShoppingCartErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(ProductId productId) {
        super(ERROR_CODE.getCode(), "Product not found: " + productId);
    }
}
