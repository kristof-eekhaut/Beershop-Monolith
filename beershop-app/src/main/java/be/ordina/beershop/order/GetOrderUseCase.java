package be.ordina.beershop.order;

import be.ordina.beershop.domain.Address;
import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.order.dto.LineItemDTO;
import be.ordina.beershop.order.dto.ShipmentAddressDTO;
import be.ordina.beershop.repository.OrderRepository;
import org.springframework.stereotype.Component;

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

    private LineItemDTO toLineItemDTO(LineItem lineItem) {
        return LineItemDTO.builder()
                .productId(lineItem.getProduct().getId().toString())
                .quantity(lineItem.getQuantity())
                .price(lineItem.getPrice())
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
