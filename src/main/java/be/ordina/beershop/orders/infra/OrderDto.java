package be.ordina.beershop.orders.infra;

import be.ordina.beershop.orders.domain.entities.Order;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class OrderDto {

    @Id
    private UUID uuid;

    private String number;

    public OrderDto() {
    }

    OrderDto(Order order) {
        this.uuid = UUID.randomUUID();
        this.number = order.getNumber();
    }
}
