package be.ordina.beershop.order;

import be.ordina.beershop.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends BusinessException {

    public static final OrderErrorCode ERROR_CODE = OrderErrorCode.ORDER_NOT_FOUND;

    public OrderNotFoundException(UUID orderId) {
        super(ERROR_CODE, "Order not found: " + orderId);
    }
}