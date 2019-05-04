package be.ordina.beershop.order;

import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
class OrderFacadeImpl implements OrderFacade {

    private final CreateOrderUseCase createOrderUseCase;

    OrderFacadeImpl(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = requireNonNull(createOrderUseCase);
    }

    @Override
    public UUID createOrder(CreateOrder createOrder) {
        return createOrderUseCase.execute(createOrder);
    }
}
