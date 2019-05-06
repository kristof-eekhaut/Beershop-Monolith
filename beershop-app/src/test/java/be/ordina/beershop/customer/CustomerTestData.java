package be.ordina.beershop.customer;

import be.ordina.beershop.domain.AddressTestData;
import be.ordina.beershop.repository.entities.Customer;

import java.time.LocalDate;
import java.util.UUID;

public class CustomerTestData {

    public static Customer.Builder manVanMelle() {
        return Customer.builder()
                .id(UUID.randomUUID())
                .name("Man van Melle")
                .birthDate(LocalDate.of(1975, 8, 2))
                .address(AddressTestData.koekoekstraat70().build());
    }
}
