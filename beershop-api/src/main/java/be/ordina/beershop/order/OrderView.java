package be.ordina.beershop.order;

import be.ordina.beershop.order.dto.LineItemDTO;
import be.ordina.beershop.order.dto.ShipmentAddressDTO;

import java.util.ArrayList;
import java.util.List;

public class OrderView {

    private String id;
    private String customerId;
    private List<LineItemDTO> lineItems;
    private String state;
    private ShipmentAddressDTO shipmentAddress;
    private String shipmentId;

    private OrderView(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        lineItems = builder.lineItems;
        state = builder.state;
        shipmentAddress = builder.shipmentAddress;
        shipmentId = builder.shipmentId;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<LineItemDTO> getLineItems() {
        return lineItems;
    }

    public String getState() {
        return state;
    }

    public ShipmentAddressDTO getShipmentAddress() {
        return shipmentAddress;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String customerId;
        private List<LineItemDTO> lineItems = new ArrayList<>();
        private String state;
        private ShipmentAddressDTO shipmentAddress;
        private String shipmentId;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder lineItems(List<LineItemDTO> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public Builder lineItem(LineItemDTO lineItem) {
            this.lineItems.add(lineItem);
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder shipmentAddress(ShipmentAddressDTO shipmentAddress) {
            this.shipmentAddress = shipmentAddress;
            return this;
        }

        public Builder shipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
            return this;
        }

        public OrderView build() {
            return new OrderView(this);
        }
    }
}
