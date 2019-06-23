package be.ordina.beershop.order;

import be.ordina.beershop.domain.Repository;

import java.util.List;

public interface OrderRepository extends Repository<OrderId, Order> {

    List<Order> findByStatus(OrderStatus shipmentRequested);
}
