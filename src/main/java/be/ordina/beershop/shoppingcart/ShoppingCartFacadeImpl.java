package be.ordina.beershop.shoppingcart;

import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
class ShoppingCartFacadeImpl implements ShoppingCartFacade {

    private final AddItemToShoppingCartUseCase addItemToShoppingCartUseCase;

    ShoppingCartFacadeImpl(AddItemToShoppingCartUseCase addItemToShoppingCartUseCase) {
        this.addItemToShoppingCartUseCase = requireNonNull(addItemToShoppingCartUseCase);
    }

    @Override
    public void addItem(UUID customerId, AddItemToShoppingCart addItemToShoppingCart) {
        addItemToShoppingCartUseCase.execute(customerId, addItemToShoppingCart);
    }
}
