package be.ordina.beershop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductFacade {

    String createProduct(CreateProductCommand command);

    void updateProductStock(UpdateProductStockCommand command);

    Page<ProductView> getAllProducts(Pageable pageable);

    void reserveItemsForOrder(ReserveItemsForOrderCommand command);
}
