package be.ordina.beershop.service;

import be.ordina.beershop.domain.Address;

public class AddressDto {

    private final String street;
    private final String number;
    private final String country;
    private final String postalCode;


    public AddressDto(final Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.country = address.getCountry();
        this.postalCode = address.getCountry();
    }
}
