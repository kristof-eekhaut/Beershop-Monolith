package be.ordina.beershop.product;

import be.ordina.beershop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
class ProductFacadeImpl implements ProductFacade {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final GetProductsUseCase getProductsUseCase;

    ProductFacadeImpl(CreateProductUseCase createProductUseCase,
                      UpdateProductStockUseCase updateProductStockUseCase,
                      GetProductsUseCase getProductsUseCase) {
        this.createProductUseCase = requireNonNull(createProductUseCase);
        this.updateProductStockUseCase = requireNonNull(updateProductStockUseCase);
        this.getProductsUseCase = requireNonNull(getProductsUseCase);
    }

    @Override
    public UUID createProduct(CreateProduct createProduct) {
        return createProductUseCase.execute(createProduct);
    }

    @Override
    public void updateProductStock(UUID productId, UpdateProductStock updateProductStock) {
        updateProductStockUseCase.execute(productId, updateProductStock);
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return getProductsUseCase.execute(pageable);
    }
}
