package be.ordina.beershop.order;

import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class LineItemTestData {

    public static final int DEFAULT_QUANTITY = 10;

    public static LineItem.Builder lineItem(Product product) {
        return LineItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(DEFAULT_QUANTITY)
                .price(product.getPrice().multiply(new BigDecimal(DEFAULT_QUANTITY)));
    }
}
