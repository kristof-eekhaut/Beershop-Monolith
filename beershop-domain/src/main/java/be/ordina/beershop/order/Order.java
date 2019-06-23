package be.ordina.beershop.order;

import be.ordina.beershop.domain.AbstractAggregateRoot;
import be.ordina.beershop.shoppingcart.CustomerId;
import be.ordina.beershop.shoppingcart.ShoppingCartId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Order extends AbstractAggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final ShoppingCartId shoppingCartId;
    private final List<OrderItem> items;
    private OrderStatus state;
    private ShipmentAddress shipmentAddress;
    private ShipmentTrackingNumber shipmentTrackingNumber;

    private Order(Builder builder) {
        super(builder.id);

        this.customerId = requireNonNull(builder.customerId);
        this.shoppingCartId = requireNonNull(builder.shoppingCartId);
        this.items = requireNonNull(builder.items);
        setState(builder.state);
        setShipmentAddress(builder.shipmentAddress);
        setShipmentTrackingNumber(builder.shipmentTrackingNumber);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public ShoppingCartId getShoppingCartId() {
        return shoppingCartId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getState() {
        return state;
    }

    public Optional<ShipmentAddress> getShipmentAddress() {
        return Optional.ofNullable(shipmentAddress);
    }

    public Optional<ShipmentTrackingNumber> getShipmentTrackingNumber() {
        return Optional.ofNullable(shipmentTrackingNumber);
    }

    public void pay() {
        setState(OrderStatus.PAID);
    }

    public void requestShipment(ShipmentTrackingNumber shipmentTrackingNumber) {
        requireNonNull(shipmentTrackingNumber);

        if (state != OrderStatus.PAID) {
            throw new IllegalStateException("Order cannot be shipped if it's not paid");
        }

        setShipmentTrackingNumber(shipmentTrackingNumber);
        setState(OrderStatus.SHIPMENT_REQUESTED);
    }

    public void delivered() {
        setState(OrderStatus.DELIVERED);
    }

    public void fail() {
        setState(OrderStatus.ORDER_FAILED);
    }

    private void setState(OrderStatus state) {
        this.state = requireNonNull(state);
    }

    private void setShipmentAddress(ShipmentAddress shipmentAddress) {
        this.shipmentAddress = requireNonNull(shipmentAddress);
    }

    private void setShipmentTrackingNumber(ShipmentTrackingNumber shipmentTrackingNumber) {
        this.shipmentTrackingNumber = shipmentTrackingNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private OrderId id;
        private CustomerId customerId;
        private ShoppingCartId shoppingCartId;
        private List<OrderItem> items = new ArrayList<>();
        private OrderStatus state;
        private ShipmentAddress shipmentAddress;
        private ShipmentTrackingNumber shipmentTrackingNumber;

        private Builder() {
        }

        public Builder id(OrderId id) {
            this.id = id;
            return this;
        }

        public Builder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder shoppingCartId(ShoppingCartId shoppingCartId) {
            this.shoppingCartId = shoppingCartId;
            return this;
        }

        public Builder state(OrderStatus state) {
            this.state = state;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public Builder item(OrderItem orderItem) {
            this.items.add(orderItem);
            return this;
        }

        public Builder shipmentAddress(ShipmentAddress shipmentAddress) {
            this.shipmentAddress = shipmentAddress;
            return this;
        }

        public Builder shipmentTrackingNumber(ShipmentTrackingNumber shipmentTrackingNumber) {
            this.shipmentTrackingNumber = shipmentTrackingNumber;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
