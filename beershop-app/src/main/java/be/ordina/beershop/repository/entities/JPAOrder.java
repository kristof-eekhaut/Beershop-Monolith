package be.ordina.beershop.repository.entities;

import be.ordina.beershop.order.OrderStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "ORDERS")
public class JPAOrder {

    @Id
    @Column(name = "ID")
    private final UUID id;

    @Column(name = "CUSTOMER_ID")
    private UUID customerId;

    @Column(name = "SHOPPING_CART_ID")
    private UUID shoppingCartId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JPAOrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus state;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Embedded
    private JPAAddress shipmentAddress;

    @Column(name = "SHIPMENT_TRACKING_NUMBER")
    private String shipmentTrackingNumber;

    public JPAOrder() {
        // For Hibernate
        this.id = null;
    }

    private JPAOrder(Builder builder) {
        this.id = requireNonNull(builder.id);
        this.createdOn = LocalDateTime.now();

        setCustomerId(builder.customerId);
        setShoppingCartId(builder.shoppingCartId);
        setItems(builder.items);
        setState(builder.state);
        setShipmentAddress(builder.shipmentAddress);
        setShipmentTrackingNumber(builder.shipmentTrackingNumber);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(UUID shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public List<JPAOrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public void setItems(List<JPAOrderItem> orderItems) {
        this.items = orderItems;
    }

    public OrderStatus getState() {
        return state;
    }

    public void setState(OrderStatus state) {
        this.state = state;
    }

    public JPAAddress getShipmentAddress() {
        return shipmentAddress;
    }

    public void setShipmentAddress(final JPAAddress shipmentAddress) {
        this.shipmentAddress = shipmentAddress;
    }

    public Optional<String> getShipmentTrackingNumber() {
        return Optional.ofNullable(shipmentTrackingNumber);
    }

    public void setShipmentTrackingNumber(String shipmentTrackingNumber) {
        this.shipmentTrackingNumber = shipmentTrackingNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private UUID customerId;
        private UUID shoppingCartId;
        private List<JPAOrderItem> items = new ArrayList<>();
        private OrderStatus state = OrderStatus.CREATED;
        private JPAAddress shipmentAddress;
        private String shipmentTrackingNumber;

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

        public Builder shoppingCartId(UUID shoppingCartId) {
            this.shoppingCartId = shoppingCartId;
            return this;
        }

        public Builder items(List<JPAOrderItem> orderItems) {
            this.items = orderItems;
            return this;
        }

        public Builder item(JPAOrderItem orderItem) {
            this.items.add(orderItem);
            return this;
        }

        public Builder state(OrderStatus state) {
            this.state = state;
            return this;
        }

        public Builder shipmentAddress(JPAAddress shipmentAddress) {
            this.shipmentAddress = shipmentAddress;
            return this;
        }

        public Builder shipmentTrackingNumber(String shipmentTrackingNumber) {
            this.shipmentTrackingNumber = shipmentTrackingNumber;
            return this;
        }

        public JPAOrder build() {
            return new JPAOrder(this);
        }
    }
}
