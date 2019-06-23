package be.ordina.beershop.order;

import be.ordina.beershop.common.Price;
import be.ordina.beershop.domain.AbstractValueObject;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.shoppingcart.exception.InvalidShoppingCartItemQuantityException;

import static java.util.Objects.requireNonNull;

public class OrderItem extends AbstractValueObject {

    private ProductId productId;
    private int quantity;
    private Price productPrice;
    private Price totalPrice;

    private OrderItem(Builder builder) {
        setProductId(builder.productId);
        setQuantity(builder.quantity);
        setProductPrice(builder.productPrice);
        setTotalPrice(builder.totalPrice);
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Price getProductPrice() {
        return productPrice;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    private void setProductId(ProductId productId) {
        this.productId = requireNonNull(productId);
    }

    private void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidShoppingCartItemQuantityException("Quantity must be higher than 0.");
        }
        this.quantity = quantity;
    }

    private void setProductPrice(Price productPrice) {
        this.productPrice = requireNonNull(productPrice);
    }

    public void setTotalPrice(Price totalPrice) {
        this.totalPrice = requireNonNull(totalPrice);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ProductId productId;
        private int quantity;
        private Price productPrice;
        private Price totalPrice;

        private Builder() {
        }

        public Builder productId(ProductId productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder productPrice(Price productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder totalPrice(Price totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
