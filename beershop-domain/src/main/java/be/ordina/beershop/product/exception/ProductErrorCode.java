package be.ordina.beershop.product.exception;

import be.ordina.beershop.domain.exception.DomainErrorCode;

public enum ProductErrorCode implements DomainErrorCode {

    INVALID_PRODUCT_QUANTITY;

    @Override
    public String getCode() {
        return "PRODUCT-" + name();
    }
}
