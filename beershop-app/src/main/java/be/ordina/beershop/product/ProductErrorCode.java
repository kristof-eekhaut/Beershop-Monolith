package be.ordina.beershop.product;

import be.ordina.beershop.exception.BusinessErrorCode;

public enum ProductErrorCode implements BusinessErrorCode {

    INVALID_PRODUCT_QUANTITY;

    @Override
    public String getCode() {
        return "PRODUCT-" + name();
    }
}
