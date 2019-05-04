package be.ordina.beershop.shoppingcart;

import java.util.UUID;

public interface ShoppingCartFacade {

    void addItem(UUID customerId, AddItemToShoppingCart addItemToShoppingCart);
}
