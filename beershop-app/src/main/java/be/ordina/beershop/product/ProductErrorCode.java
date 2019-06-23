package be.ordina.beershop.product;

import be.ordina.beershop.exception.BusinessErrorCode;

public enum ProductErrorCode implements BusinessErrorCode {

    PRODUCT_NOT_FOUND,
    INSUFFICIENT_STOCK;

    @Override
    public String getCode() {
        return "PRODUCT-" + name();
    }
}
