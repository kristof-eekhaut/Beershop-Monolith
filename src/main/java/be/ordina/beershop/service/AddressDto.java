package be.ordina.beershop.service;

import be.ordina.beershop.domain.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressDto {

    private final String street;
    private final String number;
    private final String country;
    private final String postalCode;

    @JsonCreator
    AddressDto(@JsonProperty("street") final String street,
               @JsonProperty("number") final String number,
               @JsonProperty("country") final String country,
               @JsonProperty("postalCode") final String postalCode
    ) {
        this.street = street;
        this.number = number;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public AddressDto(final Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.country = address.getCountry();
        this.postalCode = address.getCountry();
    }
}
