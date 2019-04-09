package be.ordina.beershop.service;

import be.ordina.beershop.domain.Address;

import java.math.BigDecimal;

public class ShipmentRequestDto {

    private final AddressDto address;
    private final BigDecimal weightInGrams;

    public ShipmentRequestDto(final Address address, final BigDecimal weightInGrams) {
        this.address = new AddressDto(address);
        this.weightInGrams = weightInGrams;
    }

    public AddressDto getAddress() {
        return address;
    }

    public BigDecimal getWeightInGrams() {
        return weightInGrams;
    }
}
