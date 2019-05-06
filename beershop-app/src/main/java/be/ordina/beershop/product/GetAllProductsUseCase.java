package be.ordina.beershop.product;

import be.ordina.beershop.product.dto.WeightDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
class GetAllProductsUseCase {

    private final ProductRepository productRepository;

    GetAllProductsUseCase(ProductRepository productRepository) {
        this.productRepository = requireNonNull(productRepository);
    }

    Page<ProductView> execute(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toProductDTO);
    }

    private ProductView toProductDTO(Product product) {
        return ProductView.builder()
                .id(product.getId().toString())
                .name(product.getName())
                .alcoholPercentage(product.getAlcoholPercentage())
                .weight(toWeightDTO(product.getWeight()))
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .build();
    }

    private WeightDTO toWeightDTO(Weight weight) {
        return new WeightDTO(weight.getAmount(), weight.getUnit().name());
    }
}