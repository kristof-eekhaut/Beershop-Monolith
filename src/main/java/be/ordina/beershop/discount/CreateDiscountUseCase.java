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
    void execute(CreateDiscount createDiscount) {
        Product product = productRepository.findById(createDiscount.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(createDiscount.getProductId()));

        addDiscount(product, createDiscount);
        productRepository.save(product);
    }

    private void addDiscount(Product product, CreateDiscount createDiscount) {
        Discount discount = Discount.builder()
                .id(UUID.randomUUID())
                .percentage(createDiscount.getPercentage())
                .startDate(createDiscount.getStartDate())
                .endDate(createDiscount.getEndDate())
                .build();

        product.addDiscount(discount);
    }
}