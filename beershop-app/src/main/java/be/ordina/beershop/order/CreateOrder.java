package be.ordina.beershop.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.UUID;

public class CreateOrder {

    @Valid
    private final UUID customerId;

    @JsonCreator
    public CreateOrder(@JsonProperty("customerId") final UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
