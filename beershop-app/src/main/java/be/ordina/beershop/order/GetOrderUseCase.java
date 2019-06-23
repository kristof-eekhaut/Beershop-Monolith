package be.ordina.beershop.order;

import be.ordina.beershop.order.dto.LineItemDTO;
import be.ordina.beershop.order.dto.ShipmentAddressDTO;
import be.ordina.beershop.repository.entities.JPAAddress;
import be.ordina.beershop.repository.entities.JPAOrder;
import be.ordina.beershop.repository.entities.JPAOrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
class GetOrderUseCase {

    private final JPAOrderDAO orderRepository;

    GetOrderUseCase(JPAOrderDAO orderRepository) {
        this.orderRepository = requireNonNull(orderRepository);
    }

    Optional<OrderView> execute(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId))
                .map(this::toOrderDTO);
    }

    private OrderView toOrderDTO(JPAOrder order) {
        return OrderView.builder()
                .id(order.getId().toString())
                .customerId(order.getCustomerId().toString())
                .lineItems(order.getItems().stream()
                        .map(this::toLineItemDTO)
                        .collect(Collectors.toList()))
                .state(order.getState().name())
                .shipmentAddress(toShipmentAddressDTO(order.getShipmentAddress()))
                .shipmentId(order.getShipmentTrackingNumber().orElse(null))
                .build();
    }

    private LineItemDTO toLineItemDTO(JPAOrderItem jpaOrderItem) {
        return LineItemDTO.builder()
                .productId(jpaOrderItem.getProductId().toString())
                .quantity(jpaOrderItem.getQuantity())
                .price(jpaOrderItem.getProductPrice().multiply(new BigDecimal(jpaOrderItem.getQuantity())))
                .build();
    }

    private ShipmentAddressDTO toShipmentAddressDTO(JPAAddress address) {
        return ShipmentAddressDTO.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }
}
