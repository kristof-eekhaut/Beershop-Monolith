package be.ordina.beershop.product;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Component
class UpdateProductStockUseCase {

    private final ProductRepository productRepository;

    UpdateProductStockUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    void execute(UpdateProductStockCommand command) {

        ProductId productId = ProductId.fromString(command.getProductId());
        productRepository.findById(productId).ifPresent(product -> {
            product.changeStockQuantity(command.getQuantity());
            productRepository.update(product);
        });
    }
}