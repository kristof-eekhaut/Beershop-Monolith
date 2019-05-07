package be.ordina.beershop.shoppingcart.exception;

import be.ordina.beershop.domain.exception.DomainErrorCode;

public enum ShoppingCartErrorCode implements DomainErrorCode {

    SHOPPING_CART_NOT_FOUND,
    CUSTOMER_NOT_FOUND,
    PRODUCT_NOT_FOUND,
    INVALID_SHOPPING_CART_ITEM_QUANTITY;

    @Override
    public String getCode() {
        return "SHOPPING_CART-" + name();
    }
}
