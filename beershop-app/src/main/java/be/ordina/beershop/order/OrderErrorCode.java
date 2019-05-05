package be.ordina.beershop.order;

import be.ordina.beershop.exception.BusinessErrorCode;

public enum OrderErrorCode implements BusinessErrorCode {

    ORDER_NOT_FOUND,
    CUSTOMER_NOT_FOUND;

    @Override
    public String getCode() {
        return "ORDER-" + name();
    }
}
