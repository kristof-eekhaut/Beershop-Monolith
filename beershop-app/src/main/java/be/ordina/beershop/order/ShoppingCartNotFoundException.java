package be.ordina.beershop.order;

import be.ordina.beershop.exception.BusinessException;
import be.ordina.beershop.shoppingcart.CustomerId;

public class ShoppingCartNotFoundException extends BusinessException {

    public static final OrderErrorCode ERROR_CODE = OrderErrorCode.SHOPPING_CART_NOT_FOUND;

    public ShoppingCartNotFoundException(CustomerId customerId) {
        super(ERROR_CODE.getCode(), "No shopping cart found for customer: " + customerId);
    }
}
