package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class AddItemToShoppingCartUseCase {

    private final BeerShopService beerShopService;

    AddItemToShoppingCartUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    void execute(UUID customerId, AddItemToShoppingCart addItemToShoppingCart) {
        beerShopService.createItemInShoppingCart(customerId, addItemToShoppingCart);
    }
}