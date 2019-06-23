package be.ordina.beershop.product;

import java.util.ArrayList;
import java.util.List;

public class ReserveItemsForOrderCommand {

    private final String orderId;
    private final List<ItemToReserve> itemsToReserve;

    ReserveItemsForOrderCommand(Builder builder) {
        this.orderId = builder.orderId;
        this.itemsToReserve = builder.itemsToReserve;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<ItemToReserve> getItemsToReserve() {
        return itemsToReserve;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class ItemToReserve {

        private final String productId;
        private final int quantity;

        public ItemToReserve(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static final class Builder {
        private String orderId;
        private List<ItemToReserve> itemsToReserve = new ArrayList<>();

        private Builder() {
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder itemToReserve(ItemToReserve itemToReserve) {
            this.itemsToReserve.add(itemToReserve);
            return this;
        }

        public Builder itemToReserve(String productId, int quantity) {
            return itemToReserve(new ItemToReserve(productId, quantity));
        }

        public ReserveItemsForOrderCommand build() {
            return new ReserveItemsForOrderCommand(this);
        }
    }
}
