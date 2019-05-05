package be.ordina.beershop.product;

import be.ordina.beershop.exception.BusinessException;

public class InvalidProductQuantityException extends BusinessException {

    public static final ProductErrorCode ERROR_CODE = ProductErrorCode.INVALID_PRODUCT_QUANTITY;

    public InvalidProductQuantityException() {
        super(ERROR_CODE);
    }

    public InvalidProductQuantityException(String message) {
        super(ERROR_CODE, message);
    }
}
