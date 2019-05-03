package be.ordina.beershop.order;

import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Product;

import java.util.UUID;

public class LineItemTestData {

    public static LineItem.Builder lineItem(Product product) {
        return LineItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(10)
                .price(product.getPrice());
    }
}
