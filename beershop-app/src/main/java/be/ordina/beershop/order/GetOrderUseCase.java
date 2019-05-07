package be.ordina.beershop.order;

import be.ordina.beershop.repository.entities.Address;
import be.ordina.beershop.repository.entities.JPAShoppingCartItem;
import be.ordina.beershop.repository.entities.Order;
import be.ordina.beershop.order.dto.LineItemDTO;
import be.ordina.beershop.order.dto.ShipmentAddressDTO;
import be.ordina.beershop.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
class GetOrderUseCase {

    private final OrderRepository orderRepository;

    GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = requireNonNull(orderRepository);
    }

    Optional<OrderView> execute(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId))
                .map(this::toOrderDTO);
    }

    private OrderView toOrderDTO(Order order) {
        return OrderView.builder()
                .id(order.getId().toString())
                .customerId(order.getCustomer().getId().toString())
                .lineItems(order.getLineItems().stream()
                        .map(this::toLineItemDTO)
                        .collect(Collectors.toList()))
                .state(order.getState().name())
                .shipmentAddress(toShipmentAddressDTO(order.getAddress()))
                .shipmentId(order.getShipmentId())
                .build();
    }

    private LineItemDTO toLineItemDTO(JPAShoppingCartItem lineItem) {
        return LineItemDTO.builder()
                .productId(lineItem.getProductId().toString())
                .quantity(lineItem.getQuantity())
                .price(lineItem.getProductPrice().multiply(new BigDecimal(lineItem.getQuantity())))
                .build();
    }

    private ShipmentAddressDTO toShipmentAddressDTO(Address address) {
        return ShipmentAddressDTO.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }
}
