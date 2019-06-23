package be.ordina.beershop.order;

import be.ordina.beershop.order.dto.ShipmentAddressDTO;
import be.ordina.beershop.shoppingcart.CustomerId;
import be.ordina.beershop.shoppingcart.ShoppingCart;
import be.ordina.beershop.shoppingcart.ShoppingCartItem;
import be.ordina.beershop.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Component
class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    CreateOrderUseCase(OrderRepository orderRepository,
                       ShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = requireNonNull(orderRepository);
        this.shoppingCartRepository = requireNonNull(shoppingCartRepository);
    }

    @Transactional
    String execute(CreateOrderCommand command) {

        CustomerId customerId = CustomerId.fromString(command.getCustomerId());
        final ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(customerId));

        Order order = Order.builder()
                .id(orderRepository.nextId())
                .customerId(customerId)
                .shoppingCartId(shoppingCart.getId())
                .items(shoppingCart.getItems().stream()
                        .map(this::createOrderItem)
                        .collect(toList()))
                .state(OrderStatus.CREATED)
                .shipmentAddress(toShippingAddress(command.getShipmentAddressDTO()))
                .build();

        orderRepository.add(order);

        // TODO: this should be done with an event
        shoppingCart.clear();
        shoppingCartRepository.update(shoppingCart);

        return order.getId().toString();
    }

    private ShipmentAddress toShippingAddress(ShipmentAddressDTO shipmentAddressDTO) {
        return ShipmentAddress.builder()
                .street(shipmentAddressDTO.getStreet())
                .number(shipmentAddressDTO.getNumber())
                .country(shipmentAddressDTO.getCountry())
                .postalCode(shipmentAddressDTO.getPostalCode())
                .build();
    }

    private OrderItem createOrderItem(ShoppingCartItem shoppingCartItem) {
        return OrderItem.builder()
                .productId(shoppingCartItem.getProductId())
                .quantity(shoppingCartItem.getQuantity())
                .productPrice(shoppingCartItem.getProductPrice())
                .totalPrice(shoppingCartItem.getTotalPrice())
                .build();
    }
}