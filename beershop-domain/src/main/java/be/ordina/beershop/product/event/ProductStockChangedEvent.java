package be.ordina.beershop.product.event;

import be.ordina.beershop.domain.AbstractDomainEvent;
import be.ordina.beershop.product.ProductId;

import static java.util.Objects.requireNonNull;

public class ProductStockChangedEvent extends AbstractDomainEvent {

    private final ProductId productId;
    private final int quantity;

    public ProductStockChangedEvent(ProductId productId, int quantity) {
        this.productId = requireNonNull(productId);
        this.quantity = requireNonNull(quantity);
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
