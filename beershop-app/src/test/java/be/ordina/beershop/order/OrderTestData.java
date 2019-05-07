package be.ordina.beershop.order;

import be.ordina.beershop.repository.entities.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.ordina.beershop.domain.AddressTestData.koekoekstraat70;
import static be.ordina.beershop.order.JPAShoppingCartItemTestData.shoppingCartItem;

public class OrderTestData {

    public static Order.Builder unpaidOrder(Customer customer, UUID shoppingCartId, JPAProduct... products) {

        List<JPAShoppingCartItem> lineItems = Arrays.stream(products)
                .map(product -> shoppingCartItem(shoppingCartId, product).build())
                .collect(Collectors.toList());

        return Order.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .address(koekoekstraat70().build())
                .lineItems(lineItems)
                .state(OrderStatus.CREATED)
                .createdOn(LocalDateTime.now());
    }

    public static Order.Builder paidOrder(Customer customer, UUID shoppingCartId, JPAProduct... products) {

        return unpaidOrder(customer, shoppingCartId, products)
                .state(OrderStatus.PAID);
    }
}
