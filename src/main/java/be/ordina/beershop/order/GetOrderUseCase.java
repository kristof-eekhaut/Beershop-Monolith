package be.ordina.beershop.order;

import be.ordina.beershop.domain.Order;
import be.ordina.beershop.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class GetOrderUseCase {

    private final OrderRepository orderRepository;

    GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = requireNonNull(orderRepository);
    }

    Optional<Order> execute(UUID id) {
        return orderRepository.findById(id);
    }
}
