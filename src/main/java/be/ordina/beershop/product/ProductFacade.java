package be.ordina.beershop.product;

import be.ordina.beershop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductFacade {

    UUID createProduct(CreateProduct createProduct);

    void updateProductStock(UUID productId, UpdateProductStock updateProductStock);

    Page<Product> getProducts(Pageable pageable);
}
