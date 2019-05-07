package be.ordina.beershop.shoppingcart.dto;

import java.math.BigDecimal;

public class ShoppingCartItemDTO {

    private String productId;
    private int quantity;
    private BigDecimal productPrice;
    private BigDecimal totalPrice;

    private ShoppingCartItemDTO(Builder builder) {
        productId = builder.productId;
        quantity = builder.quantity;
        productPrice = builder.productPrice;
        totalPrice = builder.totalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String productId;
        private int quantity;
        private BigDecimal productPrice;
        private BigDecimal totalPrice;

        private Builder() {
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder productPrice(BigDecimal productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public ShoppingCartItemDTO build() {
            return new ShoppingCartItemDTO(this);
        }
    }
}
