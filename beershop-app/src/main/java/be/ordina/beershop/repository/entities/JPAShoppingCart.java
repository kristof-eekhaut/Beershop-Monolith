package be.ordina.beershop.repository.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity(name = "SHOPPING_CART")
public class JPAShoppingCart {

    @Id
    @Column(name = "ID")
    private final UUID id;

    @Column(name = "CUSTOMER_ID")
    private UUID customerId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JPAShoppingCartItem> items = new ArrayList<>();

    public JPAShoppingCart() {
        // hibernate
        this.id = null;
    }

    private JPAShoppingCart(Builder builder) {
        this.id = builder.id;

        setCustomerId(builder.customerId);
        setItems(builder.items);
    }

    public UUID getId() {
        return id;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setItems(final List<JPAShoppingCartItem> items) {
        this.items = requireNonNull(items);
    }

    public List<JPAShoppingCartItem> getItems() {
        return new ArrayList<>(items);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private UUID customerId;
        private List<JPAShoppingCartItem> items = new ArrayList<>();

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder items(List<JPAShoppingCartItem> items) {
            this.items = items;
            return this;
        }

        public Builder item(JPAShoppingCartItem item) {
            this.items.add(item);
            return this;
        }

        public JPAShoppingCart build() {
            return new JPAShoppingCart(this);
        }
    }
}
