package be.ordina.beershop.order;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class RequestShipmentUseCase {

    private final BeerShopService beerShopService;

    RequestShipmentUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    void execute(UUID orderId) {
        beerShopService.requestShipment(orderId);
    }
}