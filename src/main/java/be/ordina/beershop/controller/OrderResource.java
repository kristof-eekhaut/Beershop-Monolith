package be.ordina.beershop.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class OrderResource {

    private final UUID customerId;

    @JsonCreator
    public OrderResource(@JsonProperty("customerId") final UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
