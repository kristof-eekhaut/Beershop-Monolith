package be.ordina.beershop.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CreateOrder {

    private final UUID customerId;

    @JsonCreator
    public CreateOrder(@JsonProperty("customerId") final UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
