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

@Entity(name = "SHOPPING_CART_ITEM")
public class JPAShoppingCartItem {

    @EmbeddedId
    private final JPAShoppingCartItemId id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRICE")
    private BigDecimal productPrice;

    private JPAShoppingCartItem() {
        // For hibernate
        this.id = null;
    }

    private JPAShoppingCartItem(Builder builder) {
        this.id = requireNonNull(builder.id);
        setQuantity(builder.quantity);
        setProductPrice(builder.productPrice);
    }

    public JPAShoppingCartItemId getId() {
        return id;
    }

    public UUID getProductId() {
        return id.getProductId();
    }

    public UUID getShoppingCartId() {
        return id.getShoppingCartId();
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

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private JPAShoppingCartItemId id;
        private int quantity;
        private BigDecimal productPrice;

        private Builder() {
        }

        public Builder id(JPAShoppingCartItemId id) {
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

        public JPAShoppingCartItem build() {
            return new JPAShoppingCartItem(this);
        }
    }

    @Embeddable
    public static class JPAShoppingCartItemId implements Serializable {

        @Column(name = "SHOPPING_CART_ID")
        private final UUID shoppingCartId;
        @Column(name = "PRODUCT_ID")
        private final UUID productId;

        private JPAShoppingCartItemId() {
            // For hibernate
            this.shoppingCartId = null;
            this.productId = null;
        }

        public JPAShoppingCartItemId(UUID shoppingCartId, UUID productId) {
            this.shoppingCartId = requireNonNull(shoppingCartId);
            this.productId = requireNonNull(productId);
        }

        public UUID getShoppingCartId() {
            return shoppingCartId;
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
