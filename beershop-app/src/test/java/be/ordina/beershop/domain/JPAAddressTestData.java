package be.ordina.beershop.domain;

import be.ordina.beershop.repository.entities.JPAAddress;

public class JPAAddressTestData {

    public static JPAAddress.Builder koekoekstraat70() {
        return JPAAddress.builder()
                .street("Koekoekstraat")
                .number("70")
                .postalCode("9090")
                .country("Belgium");
    }
}
