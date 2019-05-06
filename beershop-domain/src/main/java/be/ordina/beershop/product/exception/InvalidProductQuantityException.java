package be.ordina.beershop.product.exception;

import be.ordina.beershop.domain.exception.DomainException;

public class InvalidProductQuantityException extends DomainException {

    public static final ProductErrorCode ERROR_CODE = ProductErrorCode.INVALID_PRODUCT_QUANTITY;

    public InvalidProductQuantityException() {
        super(ERROR_CODE);
    }

    public InvalidProductQuantityException(String message) {
        super(ERROR_CODE, message);
    }
}
