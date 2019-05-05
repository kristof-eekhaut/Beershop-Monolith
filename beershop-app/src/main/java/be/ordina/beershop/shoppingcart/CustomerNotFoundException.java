package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.exception.EntityNotFoundException;

import java.util.UUID;

public class CustomerNotFoundException extends EntityNotFoundException {

    public static final ShoppingCartErrorCode ERROR_CODE = ShoppingCartErrorCode.CUSTOMER_NOT_FOUND;

    public CustomerNotFoundException(UUID customerId) {
        super(ERROR_CODE, "Customer not found: " + customerId);
    }
}
