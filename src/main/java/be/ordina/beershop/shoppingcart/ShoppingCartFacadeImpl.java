package be.ordina.beershop.shoppingcart;

import org.springframework.stereotype.Service;

import java.util.UUID;

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
    public void addProduct(UUID customerId, AddProductToShoppingCart addProductToShoppingCart) {
        addProductToShoppingCartUseCase.execute(customerId, addProductToShoppingCart);
    }

    @Override
    public void removeProduct(UUID customerId, UUID productId) {
        removeProductFromShoppingCartUseCase.execute(customerId, productId);
    }

    @Override
    public void changeQuantityOfProduct(UUID customerId, ChangeQuantityOfProductInShoppingCart changeQuantityOfProductInShoppingCart) {
        changeQuantityOfProductInShoppingCartUseCase.execute(customerId, changeQuantityOfProductInShoppingCart);
    }
}
