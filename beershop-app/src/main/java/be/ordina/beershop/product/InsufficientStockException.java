package be.ordina.beershop.product;

import be.ordina.beershop.exception.EntityNotFoundException;

public class InsufficientStockException extends EntityNotFoundException {

    public static final ProductErrorCode ERROR_CODE = ProductErrorCode.INSUFFICIENT_STOCK;

    public InsufficientStockException(ProductId productId, int availableQuantity, int requestedQuantity) {
        super(ERROR_CODE.getCode(), "Not enough stock for product (id: " + productId +
                "). Available: " + availableQuantity + " <  requested: " + requestedQuantity);
    }
}
