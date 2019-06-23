package be.ordina.beershop.order;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Component
class PayOrderUseCase {

    private final OrderRepository orderRepository;

    PayOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = requireNonNull(orderRepository);
    }

    @Transactional
    void execute(String orderIdString) {
        OrderId orderId = OrderId.fromString(orderIdString);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.pay();

        orderRepository.update(order);
    }
}