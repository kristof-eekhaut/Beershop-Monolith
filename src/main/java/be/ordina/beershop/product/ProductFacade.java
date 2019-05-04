package be.ordina.beershop.product;

import java.util.UUID;

public interface ProductFacade {

    UUID createProduct(CreateProduct createProduct);

    void updateProductStock(UUID productId, UpdateProductStock updateProductStock);
}
