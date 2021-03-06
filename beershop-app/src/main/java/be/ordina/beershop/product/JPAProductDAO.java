package be.ordina.beershop.product;

import be.ordina.beershop.repository.entities.JPAProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JPAProductDAO extends JpaRepository<JPAProduct, UUID> {
}
