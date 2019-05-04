package be.ordina.beershop.discount;

import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
class DiscountFacadeImpl implements DiscountFacade {

    private final CreateDiscountUseCase createDiscountUseCase;

    DiscountFacadeImpl(CreateDiscountUseCase createDiscountUseCase) {
        this.createDiscountUseCase = requireNonNull(createDiscountUseCase);
    }

    @Override
    public void createDiscount(CreateDiscount createDiscount) {
        createDiscountUseCase.execute(createDiscount);
    }
}
