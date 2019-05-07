package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.product.Product;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.product.ProductRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static be.ordina.beershop.shoppingcart.ShoppingCartProduct.shoppingCartProduct;
import static java.util.Objects.requireNonNull;

@Component
class ChangeQuantityOfProductInShoppingCartUseCase {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;  // TODO should go via ProductFacade

    ChangeQuantityOfProductInShoppingCartUseCase(ShoppingCartRepository shoppingCartRepository,
                                                 ProductRepository productRepository) {
        this.shoppingCartRepository = requireNonNull(shoppingCartRepository);
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    void execute(String customerIdString, ChangeQuantityOfProductInShoppingCartCommand command) {

        CustomerId customerId = CustomerId.fromString(customerIdString);
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(customerId));

        ProductId productId = ProductId.fromString(command.getProductId());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        shoppingCart.changeQuantityForProduct(
                shoppingCartProduct(product.getId(), product.calculateDiscountedPrice()),
                command.getQuantity()
        );

        shoppingCartRepository.update(shoppingCart);
    }
}