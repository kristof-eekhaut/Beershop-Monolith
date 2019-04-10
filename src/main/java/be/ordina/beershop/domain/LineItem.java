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


}
