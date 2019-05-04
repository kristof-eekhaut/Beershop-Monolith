package be.ordina.beershop.product;

import be.ordina.beershop.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProductQuantityException extends BusinessException {

    public InvalidProductQuantityException() {
        super(ProductErrorCode.INVALID_PRODUCT_QUANTITY);
    }

    public InvalidProductQuantityException(String message) {
        super(ProductErrorCode.INVALID_PRODUCT_QUANTITY, message);
    }
}
