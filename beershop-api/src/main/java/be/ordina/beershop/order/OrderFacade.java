package be.ordina.beershop.order;

import java.util.Optional;

public interface OrderFacade {

    String createOrder(CreateOrderCommand command);

    Optional<OrderView> getOrder(String id);

    void payOrder(String id);

    void requestShipment(String orderId);
}
