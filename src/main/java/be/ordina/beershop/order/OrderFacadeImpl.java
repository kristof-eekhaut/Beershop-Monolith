package be.ordina.beershop.order;

import be.ordina.beershop.domain.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
class OrderFacadeImpl implements OrderFacade {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final RequestShipmentUseCase requestShipmentUseCase;

    OrderFacadeImpl(CreateOrderUseCase createOrderUseCase,
                    GetOrderUseCase getOrderUseCase,
                    PayOrderUseCase payOrderUseCase,
                    RequestShipmentUseCase requestShipmentUseCase) {
        this.createOrderUseCase = requireNonNull(createOrderUseCase);
        this.getOrderUseCase = requireNonNull(getOrderUseCase);
        this.payOrderUseCase = requireNonNull(payOrderUseCase);
        this.requestShipmentUseCase = requireNonNull(requestShipmentUseCase);
    }

    @Override
    public UUID createOrder(CreateOrder createOrder) {
        return createOrderUseCase.execute(createOrder);
    }

    @Override
    public Optional<Order> getOrder(UUID id) {
        return getOrderUseCase.execute(id);
    }

    @Override
    public void payOrder(UUID id) {
        payOrderUseCase.execute(id);
    }

    @Override
    public void requestShipment(UUID orderId) {
        requestShipmentUseCase.execute(orderId);
    }
}
