package be.ordina.beershop.orders.domain.usecases;

import be.ordina.beershop.orders.domain.entities.Order;

public interface CreateOrder {

    void save(Order order);
}
