package be.ordina.beershop.order;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class CreateOrderUseCase {

    private final BeerShopService beerShopService;

    CreateOrderUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    UUID execute(CreateOrder createOrder) {
        return beerShopService.createOrder(createOrder).getId();
    }
}