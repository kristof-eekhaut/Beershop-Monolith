package be.ordina.beershop.product;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.domain.WeightUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static be.ordina.beershop.domain.Weight.weight;

public class ProductTestData {

    public static Product.Builder karmeliet() {
        return Product.builder()
                .id(UUID.randomUUID())
                .name("Karmeliet Tripel")
                .quantity(10)
                .price(new BigDecimal("1.20"))
                .alcoholPercentage(new BigDecimal("7.5"))
                .weight(weight(new BigDecimal("100"), WeightUnit.GRAM))
                .createdOn(LocalDateTime.now());
    }

    public static Product.Builder westmalle() {
        return Product.builder()
                .id(UUID.randomUUID())
                .name("Westmalle Tripel")
                .quantity(20)
                .price(new BigDecimal("1.30"))
                .alcoholPercentage(new BigDecimal("9.5"))
                .weight(weight(new BigDecimal("120"), WeightUnit.GRAM))
                .createdOn(LocalDateTime.now());
    }

    public static Product.Builder westvleteren() {
        return Product.builder()
                .id(UUID.randomUUID())
                .name("Westvleteren Blonde")
                .quantity(2)
                .price(new BigDecimal("3.20"))
                .alcoholPercentage(new BigDecimal("5.8"))
                .weight(weight(new BigDecimal("90"), WeightUnit.GRAM))
                .createdOn(LocalDateTime.now());
    }
}
