package be.ordina.beershop.product;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class CreateProductUseCase {

    private ProductRepository productRepository;

    CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    UUID execute(CreateProduct createProduct) {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(createProduct.getName())
                .quantity(createProduct.getQuantity())
                .price(createProduct.getPrice())
                .alcoholPercentage(createProduct.getAlcoholPercentage())
                .weight(createProduct.getWeight())
                .build();

        return productRepository.save(product).getId();
    }
}
