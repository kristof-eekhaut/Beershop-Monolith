package be.ordina.beershop.order;

import be.ordina.beershop.exception.BusinessException;

import java.util.UUID;

public class CustomerNotFoundException extends BusinessException {

    public static final OrderErrorCode ERROR_CODE = OrderErrorCode.CUSTOMER_NOT_FOUND;

    public CustomerNotFoundException(UUID customerId) {
        super(ERROR_CODE.getCode(), "Customer not found: " + customerId);
    }
}
