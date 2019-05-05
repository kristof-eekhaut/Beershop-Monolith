package be.ordina.beershop.repository;

import be.ordina.beershop.domain.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {
}
