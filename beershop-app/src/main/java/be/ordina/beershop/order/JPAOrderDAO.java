package be.ordina.beershop.order;

import be.ordina.beershop.repository.entities.JPAOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JPAOrderDAO extends JpaRepository<JPAOrder, UUID> {

    @Query(value = "From JPAOrder where state = :status")
    List<JPAOrder> findByStatus(@Param("status") final OrderStatus status);
}
