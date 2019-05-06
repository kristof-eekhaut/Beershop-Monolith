package be.ordina.beershop.product;

import be.ordina.beershop.domain.ValueObject;

public enum QuantityIndicator implements ValueObject {
    SOLD_OUT,
    ALMOST_SOLD_OUT,
    PLENTY_AVAILABLE
}
