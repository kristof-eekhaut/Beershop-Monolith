package be.ordina.beershop.repository.entities;

import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity(name = "ORDER_ITEM")
public class JPAOrderItem {

    @EmbeddedId
    private final JPAOrderItemId id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRODUCT_PRICE")
    private BigDecimal productPrice;

    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;

    private JPAOrderItem() {
        // For hibernate
        this.id = null;
    }

    private JPAOrderItem(Builder builder) {
        this.id = requireNonNull(builder.id);
        setQuantity(builder.quantity);
        setProductPrice(builder.productPrice);
        setTotalPrice(builder.totalPrice);
    }

    public JPAOrderItemId getId() {
        return id;
    }

    public UUID getProductId() {
        return id.getProductId();
    }

    public UUID getOrderId() {
        return id.getOrderId();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private JPAOrderItemId id;
        private int quantity;
        private BigDecimal productPrice;
        private BigDecimal totalPrice;

        private Builder() {
        }

        public Builder id(JPAOrderItemId id) {
            this.id = id;
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

        public JPAOrderItem build() {
            return new JPAOrderItem(this);
        }
    }

    @Embeddable
    public static class JPAOrderItemId implements Serializable {

        @Column(name = "ORDER_ID")
        private final UUID orderId;
        @Column(name = "PRODUCT_ID")
        private final UUID productId;

        private JPAOrderItemId() {
            // For hibernate
            this.orderId = null;
            this.productId = null;
        }

        public JPAOrderItemId(UUID orderId, UUID productId) {
            this.orderId = requireNonNull(orderId);
            this.productId = requireNonNull(productId);
        }

        public UUID getOrderId() {
            return orderId;
        }

        public UUID getProductId() {
            return productId;
        }

        @Override
        public boolean equals(Object obj) {
            return reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
        }
    }
}
