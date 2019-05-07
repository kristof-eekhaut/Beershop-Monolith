package be.ordina.beershop.product;

import be.ordina.beershop.common.Price;
import be.ordina.beershop.product.dto.WeightDTO;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

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
                .id(productRepository.nextId())
                .name(command.getName())
                .quantity(command.getQuantity())
                .price(Price.price(command.getPrice()))
                .alcoholPercentage(command.getAlcoholPercentage())
                .weight(createWeight(command.getWeight()))
                .build();

        productRepository.add(product);

        return product.getId().toString();
    }

    private Weight createWeight(WeightDTO weightDTO) {
        return Weight.weight(weightDTO.getAmount(), WeightUnit.valueOf(weightDTO.getUnit()));
    }
}
