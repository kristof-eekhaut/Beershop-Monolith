package be.ordina.beershop.order;

import be.ordina.beershop.repository.entities.JPAOrderItem;
import be.ordina.beershop.repository.entities.JPAProduct;

import java.math.BigDecimal;
import java.util.UUID;

public class JPAOrderItemTestData {

    public static final int DEFAULT_QUANTITY = 10;

    public static JPAOrderItem.Builder orderItem(UUID orderId, JPAProduct product) {
        return JPAOrderItem.builder()
                .id(new JPAOrderItem.JPAOrderItemId(orderId, product.getId()))
                .quantity(DEFAULT_QUANTITY)
                .productPrice(product.getPrice())
                .totalPrice(product.getPrice().multiply(new BigDecimal(DEFAULT_QUANTITY)));
    }
}
