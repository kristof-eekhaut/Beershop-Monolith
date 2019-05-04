package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends BusinessException {

    public static final ShoppingCartErrorCode ERROR_CODE = ShoppingCartErrorCode.CUSTOMER_NOT_FOUND;

    public CustomerNotFoundException(UUID customerId) {
        super(ERROR_CODE, "Customer not found: " + customerId);
    }
}
