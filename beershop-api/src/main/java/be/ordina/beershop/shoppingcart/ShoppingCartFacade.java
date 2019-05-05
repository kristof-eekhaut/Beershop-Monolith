package be.ordina.beershop.shoppingcart;

public interface ShoppingCartFacade {

    void addProduct(String customerId, AddProductToShoppingCartCommand command);

    void removeProduct(String customerId, String productId);

    void changeQuantityOfProduct(String customerId, ChangeQuantityOfProductInShoppingCartCommand command);
}
