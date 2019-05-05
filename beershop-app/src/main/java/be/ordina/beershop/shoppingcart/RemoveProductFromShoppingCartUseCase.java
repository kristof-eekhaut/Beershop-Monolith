package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class RemoveProductFromShoppingCartUseCase {

    private final BeerShopService beerShopService;

    RemoveProductFromShoppingCartUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    public void execute(UUID customerId, UUID productId) {
        beerShopService.deleteLineInShoppingCart(customerId, productId);
    }
}
