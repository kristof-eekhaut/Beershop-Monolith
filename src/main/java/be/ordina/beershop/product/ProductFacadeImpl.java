package be.ordina.beershop.product;

import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
class ProductFacadeImpl implements ProductFacade {

    private CreateProductUseCase createProductUseCase;
    private UpdateProductStockUseCase updateProductStockUseCase;

    ProductFacadeImpl(CreateProductUseCase createProductUseCase,
                      UpdateProductStockUseCase updateProductStockUseCase) {
        this.createProductUseCase = requireNonNull(createProductUseCase);
        this.updateProductStockUseCase = requireNonNull(updateProductStockUseCase);
    }

    @Override
    public UUID createProduct(CreateProduct createProduct) {
        return createProductUseCase.execute(createProduct);
    }

    @Override
    public void updateProductStock(UUID productId, UpdateProductStock updateProductStock) {
        updateProductStockUseCase.execute(productId, updateProductStock);
    }
}
