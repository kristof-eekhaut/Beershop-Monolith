package be.ordina.beershop.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity(name = "LINE_ITEM")
public class LineItem {

    @Id
    private UUID id;

    @JoinColumn(name = "PRODUCT_ID")
    @OneToOne
    private Product product;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRICE")
    private BigDecimal price;

    public LineItem() {
        // For hibernate
    }

    private LineItem(Builder builder) {
        setId(builder.id);
        setProduct(builder.product);
        setQuantity(builder.quantity);
        setPrice(builder.price);
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private Product product;
        private int quantity;
        private BigDecimal price;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
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

        public LineItem build() {
            return new LineItem(this);
        }
    }
}
