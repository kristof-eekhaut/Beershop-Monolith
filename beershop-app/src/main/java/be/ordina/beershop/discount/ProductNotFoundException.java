package be.ordina.beershop.discount;

import be.ordina.beershop.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends BusinessException {

    public static final DiscountErrorCode ERROR_CODE = DiscountErrorCode.PRODUCT_NOT_FOUND;

    public ProductNotFoundException(UUID productId) {
        super(ERROR_CODE, "Product not found: " + productId);
    }
}
