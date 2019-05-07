package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.repository.entities.JPAShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JPAShoppingCartDAO extends JpaRepository<JPAShoppingCart, UUID> {

    Optional<JPAShoppingCart> findByCustomerId(UUID customerId);
}
