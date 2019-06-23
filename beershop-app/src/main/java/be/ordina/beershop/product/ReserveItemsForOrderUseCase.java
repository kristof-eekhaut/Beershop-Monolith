package be.ordina.beershop.product;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Component
public class ReserveItemsForOrderUseCase {

    private final ProductRepository productRepository;

    ReserveItemsForOrderUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    void execute(ReserveItemsForOrderCommand command) {

        command.getItemsToReserve()
                .forEach(item -> reserveStock(ProductId.fromString(item.getProductId()), item.getQuantity()));
    }

    private void reserveStock(ProductId productId, int quantityToReserve) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final int availableQuantity = product.getQuantity();

        if (availableQuantity < quantityToReserve) {
            throw new InsufficientStockException(productId, availableQuantity, quantityToReserve);
        }

        final int newQuantity = availableQuantity - quantityToReserve;
        product.changeStockQuantity(newQuantity);
    }
}
