package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.exception.EntityNotFoundException;
import be.ordina.beershop.shoppingcart.exception.ShoppingCartErrorCode;

public class ShoppingCartNotFoundException extends EntityNotFoundException {

    public static final ShoppingCartErrorCode ERROR_CODE = ShoppingCartErrorCode.SHOPPING_CART_NOT_FOUND;

    public ShoppingCartNotFoundException(CustomerId customerId) {
        super(ERROR_CODE.getCode(), "No shopping cart found for customer: " + customerId);
    }
}
