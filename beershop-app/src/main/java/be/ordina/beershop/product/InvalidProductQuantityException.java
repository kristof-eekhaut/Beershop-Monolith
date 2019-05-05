package be.ordina.beershop.product;

import be.ordina.beershop.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProductQuantityException extends BusinessException {

    public static final ProductErrorCode ERROR_CODE = ProductErrorCode.INVALID_PRODUCT_QUANTITY;

    public InvalidProductQuantityException() {
        super(ERROR_CODE);
    }

    public InvalidProductQuantityException(String message) {
        super(ERROR_CODE, message);
    }
}
