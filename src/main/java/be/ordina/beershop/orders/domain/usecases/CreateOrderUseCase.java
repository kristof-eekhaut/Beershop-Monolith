package be.ordina.beershop.orders.domain.usecases;

import be.ordina.beershop.orders.domain.CreateOrderData;
import be.ordina.beershop.orders.domain.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderUseCase {

    private final CreateOrderRepository createOrder;

    public CreateOrderUseCase(CreateOrderRepository createOrder) {
        this.createOrder = createOrder;
    }

    public void createOrder(CreateOrderData order){
        Order order1 = new Order(order);
        createOrder.save(order1);
    }
}
