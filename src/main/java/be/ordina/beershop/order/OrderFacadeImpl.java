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

    OrderFacadeImpl(CreateOrderUseCase createOrderUseCase,
                    GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = requireNonNull(createOrderUseCase);
        this.getOrderUseCase = requireNonNull(getOrderUseCase);
    }

    @Override
    public UUID createOrder(CreateOrder createOrder) {
        return createOrderUseCase.execute(createOrder);
    }

    @Override
    public Optional<Order> getOrder(UUID id) {
        return getOrderUseCase.execute(id);
    }
}
