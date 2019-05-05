package be.ordina.beershop.order.dto;

import java.math.BigDecimal;

public class LineItemDTO {

    private String productId;
    private int quantity;
    private BigDecimal price;

    private LineItemDTO(Builder builder) {
        productId = builder.productId;
        quantity = builder.quantity;
        price = builder.price;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String productId;
        private int quantity;
        private BigDecimal price;

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

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public LineItemDTO build() {
            return new LineItemDTO(this);
        }
    }
}
