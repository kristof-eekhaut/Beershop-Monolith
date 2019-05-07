package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.exception.EntityNotFoundException;
import be.ordina.beershop.shoppingcart.exception.ShoppingCartErrorCode;

import java.util.UUID;

public class CustomerNotFoundException extends EntityNotFoundException {

    public static final ShoppingCartErrorCode ERROR_CODE = ShoppingCartErrorCode.CUSTOMER_NOT_FOUND;

    public CustomerNotFoundException(CustomerId customerId) {
        super(ERROR_CODE.getCode(), "Customer not found: " + customerId);
    }
}
