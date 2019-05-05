package be.ordina.beershop.discount;

import be.ordina.beershop.exception.BusinessErrorCode;

public enum DiscountErrorCode implements BusinessErrorCode {

    PRODUCT_NOT_FOUND;

    @Override
    public String getCode() {
        return "DISCOUNT-" + name();
    }
}
