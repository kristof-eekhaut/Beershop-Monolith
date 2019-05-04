package be.ordina.beershop.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "SHOPPING_CART")
public class ShoppingCart {

    @Id
    @Column(name = "ID")
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LineItem> lineItems = new ArrayList<>();

    public ShoppingCart() {
        id = UUID.randomUUID();
    }

    private ShoppingCart(Builder builder) {
        setId(builder.id);
        setLineItems(builder.lineItems);
    }

    public UUID getId() {
        return id;
    }

    public void setLineItems(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public List<LineItem> getLineItems() {
        return new ArrayList<>(lineItems);
    }

    public void addLineItem(final LineItem lineItem) {
        lineItems.add(lineItem);
    }

    public void updateLineItem(final LineItem lineItemUpdate) {
        lineItems.stream()
                 .filter(lineItem -> lineItem.getId().equals(lineItemUpdate.getId()))
                 .findAny()
                 .ifPresent(lineItem -> lineItem.setQuantity(lineItemUpdate.getQuantity()));
    }

    public void deleteLine(final UUID productId) {
        lineItems.removeIf(lineItem -> lineItem.getProduct().getId().equals(productId));
    }

    public void clear() {
        this.lineItems = new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private List<LineItem> lineItems = new ArrayList<>();

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder lineItems(List<LineItem> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public Builder lineItem(LineItem lineItem) {
            this.lineItems.add(lineItem);
            return this;
        }

        public ShoppingCart build() {
            return new ShoppingCart(this);
        }
    }
}
