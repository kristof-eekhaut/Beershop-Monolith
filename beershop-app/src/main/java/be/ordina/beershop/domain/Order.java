package be.ordina.beershop.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "ID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineItem> lineItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus state;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Embedded
    private Address address;

    @Column(name = "SHIPMENT_ID")
    private String shipmentId;

    public Order() {
        // For Hibernate
    }

    private Order(Builder builder) {
        setId(builder.id);
        setCustomer(builder.customer);
        setLineItems(builder.lineItems);
        setState(builder.state);
        setCreatedOn(builder.createdOn);
        setAddress(builder.address);
        setShipmentId(builder.shipmentId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<LineItem> getLineItems() {
        return new ArrayList<>(lineItems);
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OrderStatus getState() {
        return state;
    }

    public void setState(OrderStatus state) {
        this.state = state;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public void setShipmentId(final String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private Customer customer;
        private List<LineItem> lineItems = new ArrayList<>();
        private OrderStatus state;
        private LocalDateTime createdOn;
        private Address address;
        private String shipmentId;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
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

        public Builder state(OrderStatus state) {
            this.state = state;
            return this;
        }

        public Builder createdOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder shipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
