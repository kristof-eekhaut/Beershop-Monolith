package be.ordina.beershop.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipmentResponseDto {

    private final String shipmentId;
    private final String status;

    @JsonCreator
    public ShipmentResponseDto(@JsonProperty("shipmentId") final String shipmentId,
                               @JsonProperty("status") final String status) {
        this.shipmentId = shipmentId;
        this.status = status;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public boolean isDelivered() {
        return "DELIVERED".equals(status);
    }
}
