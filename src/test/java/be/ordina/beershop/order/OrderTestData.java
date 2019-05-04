package be.ordina.beershop.order;

import be.ordina.beershop.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.ordina.beershop.common.AddressTestData.koekoekstraat70;
import static be.ordina.beershop.order.LineItemTestData.lineItem;

public class OrderTestData {

    public static Order.Builder unpaidOrder(Customer customer, Product... products) {

        List<LineItem> lineItems = Arrays.stream(products)
                .map(product -> lineItem(product).build())
                .collect(Collectors.toList());

        return Order.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .address(koekoekstraat70().build())
                .lineItems(lineItems)
                .state(OrderStatus.CREATED)
                .createdOn(LocalDateTime.now());
    }

    public static Order.Builder paidOrder(Customer customer, Product... products) {

        return unpaidOrder(customer, products)
                .state(OrderStatus.PAID);
    }
}
