package be.ordina.beershop.common;

import be.ordina.beershop.domain.Address;

public class AddressTestData {

    public static Address.Builder koekoekstraat70() {
        return Address.builder()
                .street("Koekoekstraat")
                .number("70")
                .postalCode("9090")
                .country("Belgium");
    }
}
