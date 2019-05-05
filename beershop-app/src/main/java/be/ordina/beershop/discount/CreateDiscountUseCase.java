package be.ordina.beershop.discount;

import be.ordina.beershop.domain.Discount;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class CreateDiscountUseCase {

    private final ProductRepository productRepository;

    CreateDiscountUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    void execute(CreateDiscountCommand command) {
        UUID productId = UUID.fromString(command.getProductId());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        addDiscount(product, command);
        productRepository.save(product);
    }

    private void addDiscount(Product product, CreateDiscountCommand command) {
        Discount discount = Discount.builder()
                .id(UUID.randomUUID())
                .percentage(command.getPercentage())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .build();

        product.addDiscount(discount);
    }
}