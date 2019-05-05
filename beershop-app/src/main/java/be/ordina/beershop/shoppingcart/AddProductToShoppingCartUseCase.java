package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class AddProductToShoppingCartUseCase {

    private final BeerShopService beerShopService;

    AddProductToShoppingCartUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    void execute(UUID customerId, AddProductToShoppingCart addProductToShoppingCart) {
        beerShopService.createItemInShoppingCart(customerId, addProductToShoppingCart);
    }
}