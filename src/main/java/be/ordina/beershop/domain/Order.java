package be.ordina.beershop.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;
    private UUID accountId;
    private UUID paymentProviderId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<Item> lineItems;
    @Enumerated(EnumType.STRING)
    private OrderStatus state;
    private LocalDateTime createdOn;

    public Order() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getPaymentProviderId() {
        return paymentProviderId;
    }

    public void setPaymentProviderId(UUID paymentProviderId) {
        this.paymentProviderId = paymentProviderId;
    }

    public Set<Item> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<Item> lineItems) {
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
}
