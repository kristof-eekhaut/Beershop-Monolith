package be.ordina.beershop.repository;

import be.ordina.beershop.repository.entities.Order;
import be.ordina.beershop.repository.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = "From Order where state = :status")
    List<Order> findByStatus(@Param("status") final OrderStatus status);
}
