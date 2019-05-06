package be.ordina.beershop.product;

import be.ordina.beershop.domain.exception.DomainException;

class InvalidProductQuantityException extends DomainException {

    public static final ProductErrorCode ERROR_CODE = ProductErrorCode.INVALID_PRODUCT_QUANTITY;

    InvalidProductQuantityException() {
        super(ERROR_CODE);
    }

    InvalidProductQuantityException(String message) {
        super(ERROR_CODE, message);
    }
}
