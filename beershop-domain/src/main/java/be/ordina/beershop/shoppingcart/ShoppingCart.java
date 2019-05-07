package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.common.Price;
import be.ordina.beershop.domain.AbstractAggregateRoot;
import be.ordina.beershop.product.ProductId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ShoppingCart extends AbstractAggregateRoot<ShoppingCartId> {

    private final CustomerId customerId;
    private final List<ShoppingCartItem> items = new ArrayList<>();

    private ShoppingCart(Builder builder) {
        super(builder.id);
        customerId = builder.customerId;
        items.addAll(builder.shoppingCartItems);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<ShoppingCartItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addProduct(ShoppingCartProduct product, int quantity) {
        requireNonNull(product);

        ProductId productId = product.getProductId();
        findItemForProduct(productId).ifPresentOrElse(
                item -> item.addQuantity(quantity),
                () -> addItem(product, quantity)
        );
    }

    public void changeQuantityForProduct(ShoppingCartProduct product, int quantity) {
        requireNonNull(product);

        ProductId productId = product.getProductId();
        findItemForProduct(productId).ifPresentOrElse(
                item -> item.changeQuantity(quantity),
                () -> addItem(product, quantity)
        );
    }

    public void removeProduct(ProductId productId) {
        findItemForProduct(productId)
                .ifPresent(items::remove);
    }

    public Price calculateTotalPrice() {
        return items.stream()
                .map(ShoppingCartItem::getTotalPrice)
                .reduce(Price.ZERO, Price::add);
    }

    private Optional<ShoppingCartItem> findItemForProduct(ProductId productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findAny();
    }

    private void addItem(ShoppingCartProduct product, int quantity) {
        ShoppingCartItem newItem = ShoppingCartItem.builder()
                .shoppingCartId(getId())
                .productId(product.getProductId())
                .quantity(quantity)
                .productPrice(product.getDiscountedPrice())
                .build();
        items.add(newItem);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ShoppingCartId id;
        private CustomerId customerId;
        private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

        private Builder() {
        }

        public Builder id(ShoppingCartId id) {
            this.id = id;
            return this;
        }

        public Builder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder shoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
            this.shoppingCartItems = shoppingCartItems;
            return this;
        }

        public Builder shoppingCartItem(ShoppingCartItem shoppingCartItem) {
            this.shoppingCartItems.add(shoppingCartItem);
            return this;
        }

        public ShoppingCart build() {
            return new ShoppingCart(this);
        }
    }
}
