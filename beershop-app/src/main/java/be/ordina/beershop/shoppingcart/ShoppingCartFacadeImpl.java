package be.ordina.beershop.shoppingcart;

import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
class ShoppingCartFacadeImpl implements ShoppingCartFacade {

    private final AddProductToShoppingCartUseCase addProductToShoppingCartUseCase;
    private final RemoveProductFromShoppingCartUseCase removeProductFromShoppingCartUseCase;
    private final ChangeQuantityOfProductInShoppingCartUseCase changeQuantityOfProductInShoppingCartUseCase;

    ShoppingCartFacadeImpl(AddProductToShoppingCartUseCase addProductToShoppingCartUseCase,
                           RemoveProductFromShoppingCartUseCase removeProductFromShoppingCartUseCase,
                           ChangeQuantityOfProductInShoppingCartUseCase changeQuantityOfProductInShoppingCartUseCase) {
        this.addProductToShoppingCartUseCase = requireNonNull(addProductToShoppingCartUseCase);
        this.removeProductFromShoppingCartUseCase = requireNonNull(removeProductFromShoppingCartUseCase);
        this.changeQuantityOfProductInShoppingCartUseCase = requireNonNull(changeQuantityOfProductInShoppingCartUseCase);
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
