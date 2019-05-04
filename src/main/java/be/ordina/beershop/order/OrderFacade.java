package be.ordina.beershop.order;

import be.ordina.beershop.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderFacade {

    UUID createOrder(CreateOrder createOrder);

    Optional<Order> getOrder(UUID id);
}
