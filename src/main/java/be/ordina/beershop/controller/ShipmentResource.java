package be.ordina.beershop.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ShipmentResource {

    private final UUID orderId;

    @JsonCreator
    public ShipmentResource(@JsonProperty("orderId") final UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
