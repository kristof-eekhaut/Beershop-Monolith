package be.ordina.beershop.order;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class PayOrderUseCase {

    private final BeerShopService beerShopService;

    PayOrderUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    void execute(String orderId) {
        beerShopService.payOrder(UUID.fromString(orderId));
    }
}