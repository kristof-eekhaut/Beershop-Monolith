package be.ordina.beershop.order;

import be.ordina.beershop.order.dto.ShipmentAddressDTO;

public class CreateOrderCommand {

    private final String customerId;
    private final ShipmentAddressDTO shipmentAddressDTO;

    public CreateOrderCommand(final String customerId,
                              final ShipmentAddressDTO shipmentAddressDTO) {
        this.customerId = customerId;
        this.shipmentAddressDTO = shipmentAddressDTO;
    }

    public String getCustomerId() {
        return customerId;
    }

    public ShipmentAddressDTO getShipmentAddressDTO() {
        return shipmentAddressDTO;
    }
}
