package be.ordina.beershop.product;

import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
class ProductFacadeImpl implements ProductFacade {

    private CreateProductUseCase createProductUseCase;

    ProductFacadeImpl(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = requireNonNull(createProductUseCase);
    }

    @Override
    public UUID createProduct(CreateProduct createProduct) {
        return createProductUseCase.execute(createProduct);
    }
}
