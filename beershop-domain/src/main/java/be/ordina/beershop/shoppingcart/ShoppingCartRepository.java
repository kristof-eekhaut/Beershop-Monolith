package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.domain.Repository;

import java.util.Optional;

public interface ShoppingCartRepository extends Repository<ShoppingCartId, ShoppingCart> {

    Optional<ShoppingCart> findByCustomerId(CustomerId fromString);
}
