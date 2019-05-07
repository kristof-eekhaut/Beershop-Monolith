package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.shoppingcart.dto.ShoppingCartItemDTO;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartView {

    private String id;
    private String customerId;
    private List<ShoppingCartItemDTO> items;
    private BigDecimal totalPrice;

    private ShoppingCartView(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        items = builder.items;
        totalPrice = builder.totalPrice;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<ShoppingCartItemDTO> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String customerId;
        private List<ShoppingCartItemDTO> items;
        private BigDecimal totalPrice;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder items(List<ShoppingCartItemDTO> items) {
            this.items = items;
            return this;
        }

        public Builder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public ShoppingCartView build() {
            return new ShoppingCartView(this);
        }
    }
}
