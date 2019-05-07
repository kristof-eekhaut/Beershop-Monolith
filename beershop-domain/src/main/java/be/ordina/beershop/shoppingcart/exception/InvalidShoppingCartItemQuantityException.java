package be.ordina.beershop.shoppingcart.exception;

import be.ordina.beershop.domain.exception.DomainException;

public class InvalidShoppingCartItemQuantityException extends DomainException {

    public static final ShoppingCartErrorCode ERROR_CODE = ShoppingCartErrorCode.INVALID_SHOPPING_CART_ITEM_QUANTITY;

    public InvalidShoppingCartItemQuantityException() {
        super(ERROR_CODE);
    }

    public InvalidShoppingCartItemQuantityException(String message) {
        super(ERROR_CODE, message);
    }
}
