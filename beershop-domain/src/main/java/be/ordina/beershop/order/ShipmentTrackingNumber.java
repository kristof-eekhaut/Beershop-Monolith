package be.ordina.beershop.order;

import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.domain.SingleValueObject;

public class ShipmentTrackingNumber extends SingleValueObject<String> implements Identifier {

    private ShipmentTrackingNumber(String value) {
        super(value);
    }

    public static ShipmentTrackingNumber shipmentTrackingNumber(String value) {
        return new ShipmentTrackingNumber(value);
    }
}
