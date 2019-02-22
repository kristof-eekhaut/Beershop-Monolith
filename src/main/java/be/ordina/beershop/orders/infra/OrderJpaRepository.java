package be.ordina.beershop.orders.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderDto, UUID> {
}
