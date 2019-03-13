package be.ordina.beershop.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
public class Order {

    @Id
    private UUID id;
    private UUID accountId;
    private UUID paymentProviderId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<Item> lineItems;
    private String state;
    private LocalDateTime createdOn;

    public Order() {
    }

    public Order(UUID id, UUID accountId, UUID paymentProviderId, Set<Item> lineItems) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.paymentProviderId = paymentProviderId;
        this.lineItems = lineItems;
        this.state = OrderStatus.ORDER_CREATED.name();
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
