package be.ordina.beershop.domain;

import be.ordina.beershop.repository.entities.Address;

public class AddressTestData {

    public static Address.Builder koekoekstraat70() {
        return Address.builder()
                .street("Koekoekstraat")
                .number("70")
                .postalCode("9090")
                .country("Belgium");
    }
}
