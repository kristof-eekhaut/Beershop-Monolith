package be.ordina.beershop.orders.infra;

import be.ordina.beershop.orders.domain.entities.Order;
import be.ordina.beershop.orders.domain.usecases.CreateOrder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements CreateOrder {

    private OrderJpaRepository repository;

    public OrderRepositoryImpl(OrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Order order) {
      OrderDto orderDto = new OrderDto(order);
      repository.save(orderDto);
    }
}
