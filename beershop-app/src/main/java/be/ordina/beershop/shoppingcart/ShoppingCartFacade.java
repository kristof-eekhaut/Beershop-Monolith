package be.ordina.beershop.shoppingcart;

import java.util.UUID;

public interface ShoppingCartFacade {

    void addProduct(UUID customerId, AddProductToShoppingCart addProductToShoppingCart);

    void removeProduct(UUID customerId, UUID productId);

    void changeQuantityOfProduct(UUID customerId, ChangeQuantityOfProductInShoppingCart changeQuantityOfProductInShoppingCart);
}
