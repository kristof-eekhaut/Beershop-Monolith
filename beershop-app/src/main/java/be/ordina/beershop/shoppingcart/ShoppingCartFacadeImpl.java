package be.ordina.beershop.shoppingcart;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
class ShoppingCartFacadeImpl implements ShoppingCartFacade {

    private final GetShoppingCartUseCase getShoppingCartUseCase;
    private final AddProductToShoppingCartUseCase addProductToShoppingCartUseCase;
    private final RemoveProductFromShoppingCartUseCase removeProductFromShoppingCartUseCase;
    private final ChangeQuantityOfProductInShoppingCartUseCase changeQuantityOfProductInShoppingCartUseCase;

    ShoppingCartFacadeImpl(GetShoppingCartUseCase getShoppingCartUseCase,
                           AddProductToShoppingCartUseCase addProductToShoppingCartUseCase,
                           RemoveProductFromShoppingCartUseCase removeProductFromShoppingCartUseCase,
                           ChangeQuantityOfProductInShoppingCartUseCase changeQuantityOfProductInShoppingCartUseCase) {
        this.getShoppingCartUseCase = requireNonNull(getShoppingCartUseCase);
        this.addProductToShoppingCartUseCase = requireNonNull(addProductToShoppingCartUseCase);
        this.removeProductFromShoppingCartUseCase = requireNonNull(removeProductFromShoppingCartUseCase);
        this.changeQuantityOfProductInShoppingCartUseCase = requireNonNull(changeQuantityOfProductInShoppingCartUseCase);
    }

    @Override
    public Optional<ShoppingCartView> getShoppingCart(String customerId) {
        return getShoppingCartUseCase.execute(customerId);
    }

    @Override
    public void addProduct(String customerId, AddProductToShoppingCartCommand command) {
        addProductToShoppingCartUseCase.execute(customerId, command);
    }

    @Override
    public void removeProduct(String customerId, String productId) {
        removeProductFromShoppingCartUseCase.execute(customerId, productId);
    }

    @Override
    public void changeQuantityOfProduct(String customerId, ChangeQuantityOfProductInShoppingCartCommand command) {
        changeQuantityOfProductInShoppingCartUseCase.execute(customerId, command);
    }
}
