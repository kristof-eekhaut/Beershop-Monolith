package be.ordina.beershop.order;

import be.ordina.beershop.exception.EntityNotFoundException;

import java.util.UUID;

public class OrderNotFoundException extends EntityNotFoundException {

    public static final OrderErrorCode ERROR_CODE = OrderErrorCode.ORDER_NOT_FOUND;

    public OrderNotFoundException(UUID orderId) {
        super(ERROR_CODE, "Order not found: " + orderId);
    }
}