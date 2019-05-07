package be.ordina.beershop.shoppingcart;

import java.util.Optional;

public interface ShoppingCartFacade {

    Optional<ShoppingCartView> getShoppingCart(String customerId);

    void addProduct(String customerId, AddProductToShoppingCartCommand command);

    void removeProduct(String customerId, String productId);

    void changeQuantityOfProduct(String customerId, ChangeQuantityOfProductInShoppingCartCommand command);
}
