package be.ordina.beershop.discount;

import be.ordina.beershop.product.Product;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.product.ProductRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Component
class CreateDiscountUseCase {

    private final ProductRepository productRepository;

    CreateDiscountUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    void execute(CreateDiscountCommand command) {
        ProductId productId = ProductId.fromString(command.getProductId());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.addDiscount(
                command.getPercentage(),
                command.getStartDate(),
                command.getEndDate()
        );
        productRepository.update(product);
    }
}