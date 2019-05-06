package be.ordina.beershop.product;

import be.ordina.beershop.repository.entities.JPAProduct;

import java.math.BigDecimal;
import java.util.UUID;

import static be.ordina.beershop.repository.entities.JPAWeight.weight;

public class JPAProductTestData {

    public static JPAProduct.Builder karmeliet() {
        return JPAProduct.builder()
                .id(UUID.randomUUID())
                .name("Karmeliet Tripel")
                .quantity(10)
                .price(new BigDecimal("1.20"))
                .alcoholPercentage(new BigDecimal("7.5"))
                .weight(weight(new BigDecimal("100"), WeightUnit.GRAM));
    }

    public static JPAProduct.Builder westmalle() {
        return JPAProduct.builder()
                .id(UUID.randomUUID())
                .name("Westmalle Tripel")
                .quantity(20)
                .price(new BigDecimal("1.30"))
                .alcoholPercentage(new BigDecimal("9.5"))
                .weight(weight(new BigDecimal("120"), WeightUnit.GRAM));
    }

    public static JPAProduct.Builder westvleteren() {
        return JPAProduct.builder()
                .id(UUID.randomUUID())
                .name("Westvleteren Blonde")
                .quantity(2)
                .price(new BigDecimal("3.20"))
                .alcoholPercentage(new BigDecimal("5.8"))
                .weight(weight(new BigDecimal("90"), WeightUnit.GRAM));
    }
}
