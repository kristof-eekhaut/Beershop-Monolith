package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.product.Product;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.product.ProductRepository;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.entities.Customer;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static be.ordina.beershop.shoppingcart.ShoppingCartProduct.shoppingCartProduct;
import static java.util.Objects.requireNonNull;

@Component
class AddProductToShoppingCartUseCase {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;  // TODO should go via ProductFacade

    AddProductToShoppingCartUseCase(ShoppingCartRepository shoppingCartRepository,
                                    CustomerRepository customerRepository,
                                    ProductRepository productRepository) {
        this.shoppingCartRepository = requireNonNull(shoppingCartRepository);
        this.customerRepository = requireNonNull(customerRepository);
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    void execute(String customerIdString, AddProductToShoppingCartCommand command) {

        CustomerId customerId = CustomerId.fromString(customerIdString);
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customerId)
                .orElseGet(() -> createEmptyShoppingCart(customerId));

        ProductId productId = ProductId.fromString(command.getProductId());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        shoppingCart.addProduct(
                shoppingCartProduct(product.getId(), product.calculateDiscountedPrice()),
                command.getQuantity()
        );

//        shoppingCart.validate();
// TODO add validation for shopping cart
//        if (customerIsOldEnoughForProduct(lineItem, customer)) {
//            throw new RuntimeException("No underage drinking allowed");
//        }

        shoppingCartRepository.update(shoppingCart);
    }

    private ShoppingCart createEmptyShoppingCart(CustomerId customerId) {
        Customer customer = customerRepository.findById(customerId.getValue())
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        ShoppingCart shoppingCart = ShoppingCart.builder()
                .id(shoppingCartRepository.nextId())
                .customerId(customerId)
                .build();
        shoppingCartRepository.add(shoppingCart);
        return shoppingCart;
    }
}