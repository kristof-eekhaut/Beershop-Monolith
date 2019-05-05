package be.ordina.beershop.product;

import be.ordina.beershop.repository.ProductRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class UpdateProductStockUseCase {

    private final ProductRepository productRepository;

    UpdateProductStockUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    void execute(UpdateProductStockCommand command) {

        if (command.getQuantity() < 0) {
            throw new InvalidProductQuantityException("Quantity can not be less than 0.");
        }

        UUID productId = UUID.fromString(command.getProductId());
        productRepository.findById(productId).ifPresent(product -> {
            product.setQuantity(command.getQuantity());
            productRepository.save(product);
        });
    }
}