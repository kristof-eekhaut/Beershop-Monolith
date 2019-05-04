package be.ordina.beershop.order;

import java.util.UUID;

public interface OrderFacade {

    UUID createOrder(CreateOrder createOrder);
}
