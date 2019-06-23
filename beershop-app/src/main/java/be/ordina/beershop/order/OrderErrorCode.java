package be.ordina.beershop.order;

import be.ordina.beershop.exception.BusinessErrorCode;

public enum OrderErrorCode implements BusinessErrorCode {

    ORDER_NOT_FOUND,
    SHOPPING_CART_NOT_FOUND;

    @Override
    public String getCode() {
        return "ORDER-" + name();
    }
}
