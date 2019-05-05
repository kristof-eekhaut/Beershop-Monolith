package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.exception.BusinessErrorCode;

public enum ShoppingCartErrorCode implements BusinessErrorCode {

    CUSTOMER_NOT_FOUND,
    PRODUCT_NOT_FOUND;

    @Override
    public String getCode() {
        return "SHOPPING_CART-" + name();
    }
}
