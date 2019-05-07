package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.product.ProductRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Component
class RemoveProductFromShoppingCartUseCase {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;  // TODO should go via ProductFacade

    RemoveProductFromShoppingCartUseCase(ShoppingCartRepository shoppingCartRepository,
                                         ProductRepository productRepository) {
        this.shoppingCartRepository = requireNonNull(shoppingCartRepository);
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    public void execute(String customerIdString, String productId) {

        CustomerId customerId = CustomerId.fromString(customerIdString);
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(customerId));

        shoppingCart.removeProduct(ProductId.fromString(productId));

        shoppingCartRepository.update(shoppingCart);
    }
}
