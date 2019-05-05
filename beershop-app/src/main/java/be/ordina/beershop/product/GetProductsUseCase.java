package be.ordina.beershop.product;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
class GetProductsUseCase {

    private final ProductRepository productRepository;

    GetProductsUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    Page<Product> execute(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}