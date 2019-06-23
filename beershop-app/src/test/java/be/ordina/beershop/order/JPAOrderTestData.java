package be.ordina.beershop.order;

import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.JPAOrder;
import be.ordina.beershop.repository.entities.JPAOrderItem;
import be.ordina.beershop.repository.entities.JPAProduct;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.ordina.beershop.domain.JPAAddressTestData.koekoekstraat70;
import static be.ordina.beershop.order.JPAOrderItemTestData.orderItem;

public class JPAOrderTestData {

    public static JPAOrder.Builder unpaidOrder(Customer customer, UUID shoppingCartId, JPAProduct... products) {

        List<JPAOrderItem> orderItems = Arrays.stream(products)
                .map(product -> orderItem(shoppingCartId, product).build())
                .collect(Collectors.toList());

        return JPAOrder.builder()
                .id(UUID.randomUUID())
                .customerId(customer.getId())
                .shoppingCartId(shoppingCartId)
                .shipmentAddress(koekoekstraat70().build())
                .items(orderItems);
    }

    public static JPAOrder.Builder paidOrder(Customer customer, UUID shoppingCartId, JPAProduct... products) {

        return unpaidOrder(customer, shoppingCartId, products)
                .state(OrderStatus.PAID);
    }
}
