package be.ordina.beershop.order;

import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public String createOrder(CreateOrderCommand command) {
        return createOrderUseCase.execute(command);
    }

    @Override
    public Optional<OrderView> getOrder(String orderId) {
        return getOrderUseCase.execute(orderId);
    }

    @Override
    public void payOrder(String orderId) {
        payOrderUseCase.execute(orderId);
    }

    @Override
    public void requestShipment(String orderId) {
        requestShipmentUseCase.execute(orderId);
    }
}
