package be.ordina.beershop.order;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Component
class CreateOrderUseCase {

    private final BeerShopService beerShopService;

    CreateOrderUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    String execute(CreateOrderCommand command) {
        return beerShopService.createOrder(command).getId().toString();
    }
}