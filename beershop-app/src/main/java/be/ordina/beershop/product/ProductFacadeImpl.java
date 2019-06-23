package be.ordina.beershop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
class ProductFacadeImpl implements ProductFacade {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final ReserveItemsForOrderUseCase reserveItemsForOrderUseCase;

    ProductFacadeImpl(CreateProductUseCase createProductUseCase,
                      UpdateProductStockUseCase updateProductStockUseCase,
                      GetAllProductsUseCase getAllProductsUseCase,
                      ReserveItemsForOrderUseCase reserveItemsForOrderUseCase) {
        this.createProductUseCase = requireNonNull(createProductUseCase);
        this.updateProductStockUseCase = requireNonNull(updateProductStockUseCase);
        this.getAllProductsUseCase = requireNonNull(getAllProductsUseCase);
        this.reserveItemsForOrderUseCase = requireNonNull(reserveItemsForOrderUseCase);
    }

    @Override
    public String createProduct(CreateProductCommand command) {
        return createProductUseCase.execute(command);
    }

    @Override
    public void updateProductStock(UpdateProductStockCommand command) {
        updateProductStockUseCase.execute(command);
    }

    public Page<ProductView> getAllProducts(Pageable pageable) {
        return getAllProductsUseCase.execute(pageable);
    }

    @Override
    public void reserveItemsForOrder(ReserveItemsForOrderCommand command) {
        reserveItemsForOrderUseCase.execute(command);
    }
}
