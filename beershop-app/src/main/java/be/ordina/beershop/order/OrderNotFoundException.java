package be.ordina.beershop.order;

import be.ordina.beershop.exception.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {

    public static final OrderErrorCode ERROR_CODE = OrderErrorCode.ORDER_NOT_FOUND;

    public OrderNotFoundException(OrderId orderId) {
        super(ERROR_CODE.getCode(), "Order not found: " + orderId);
    }
}