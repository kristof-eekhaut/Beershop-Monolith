package be.ordina.beershop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "LINE_ITEM")
public class LineItem {

    @Id
    private UUID id;

    @JoinColumn(name = "PRODUCT_ID")
    @OneToOne
    @NotNull
    private Product product;

    @Column(name = "QUANTITY")
    @Min(value = 1)
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

        public Builder product(@NotNull Product product) {
            this.product = product;
            return this;
        }

        public Builder quantity(@Min(value = 1) int quantity) {
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
