package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.common.Price;
import be.ordina.beershop.domain.AbstractEntity;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.shoppingcart.exception.InvalidShoppingCartItemQuantityException;

import static be.ordina.beershop.shoppingcart.ShoppingCartItemId.shoppingCartItemId;
import static java.util.Objects.requireNonNull;

public class ShoppingCartItem extends AbstractEntity<ShoppingCartItemId> {

    private int quantity;
    private Price productPrice;

    private ShoppingCartItem(Builder builder) {
        super(shoppingCartItemId(builder.shoppingCartId, builder.productId));
        setQuantity(builder.quantity);
        setProductPrice(builder.productPrice);
    }

    public ProductId getProductId() {
        return getId().getProductId();
    }

    public int getQuantity() {
        return quantity;
    }

    public Price getProductPrice() {
        return productPrice;
    }

    public Price getTotalPrice() {
        return productPrice.multiply(quantity);
    }

    void changeQuantity(int quantity) {
        setQuantity(quantity);
    }

    void addQuantity(int quantityToAdd) {
        setQuantity(this.quantity + quantityToAdd);
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ShoppingCartId shoppingCartId;
        private ProductId productId;
        private int quantity;
        private Price productPrice;

        private Builder() {
        }

        public Builder shoppingCartId(ShoppingCartId shoppingCartId) {
            this.shoppingCartId = shoppingCartId;
            return this;
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

        public ShoppingCartItem build() {
            return new ShoppingCartItem(this);
        }
    }
}
