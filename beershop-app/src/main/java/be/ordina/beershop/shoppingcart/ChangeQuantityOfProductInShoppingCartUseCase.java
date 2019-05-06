package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class ChangeQuantityOfProductInShoppingCartUseCase {

    private final BeerShopService beerShopService;

    ChangeQuantityOfProductInShoppingCartUseCase(BeerShopService beerShopService) {
        this.beerShopService = requireNonNull(beerShopService);
    }

    @Transactional
    void execute(String customerId, ChangeQuantityOfProductInShoppingCartCommand command) {
        beerShopService.updateQuantityOfItemInShoppingCart(UUID.fromString(customerId), command);
    }
}