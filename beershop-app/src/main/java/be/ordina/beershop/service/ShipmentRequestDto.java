package be.ordina.beershop.service;

import java.math.BigDecimal;

public class ShipmentRequestDto {

    private final AddressDto address;
    private final BigDecimal weightInGrams;

    public ShipmentRequestDto(final AddressDto address, final BigDecimal weightInGrams) {
        this.address = address;
        this.weightInGrams = weightInGrams;
    }

    public AddressDto getAddress() {
        return address;
    }

    public BigDecimal getWeightInGrams() {
        return weightInGrams;
    }
}
