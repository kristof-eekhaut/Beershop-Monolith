package be.ordina.beershop.product;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.domain.Weight;
import be.ordina.beershop.domain.WeightUnit;
import be.ordina.beershop.product.dto.WeightDTO;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
class CreateProductUseCase {

    private final ProductRepository productRepository;

    CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    @Transactional
    String execute(CreateProductCommand command) {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .quantity(command.getQuantity())
                .price(command.getPrice())
                .alcoholPercentage(command.getAlcoholPercentage())
                .weight(createWeight(command.getWeight()))
                .build();

        return productRepository.save(product).getId().toString();
    }

    private Weight createWeight(WeightDTO weightDTO) {
        return Weight.weight(weightDTO.getAmount(), WeightUnit.valueOf(weightDTO.getUnit()));
    }
}
