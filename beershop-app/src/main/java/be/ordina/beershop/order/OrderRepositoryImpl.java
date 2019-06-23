package be.ordina.beershop.order;

import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.repository.AbstractJPARepository;
import be.ordina.beershop.repository.entities.JPAAddress;
import be.ordina.beershop.repository.entities.JPAOrder;
import be.ordina.beershop.repository.entities.JPAOrderItem;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static be.ordina.beershop.common.Price.price;
import static be.ordina.beershop.order.OrderId.orderId;
import static be.ordina.beershop.product.ProductId.productId;
import static be.ordina.beershop.shoppingcart.CustomerId.customerId;
import static be.ordina.beershop.shoppingcart.ShoppingCartId.shoppingCartId;
import static java.util.stream.Collectors.toList;

@Component
public class OrderRepositoryImpl extends AbstractJPARepository<OrderId, Order, UUID, JPAOrder>
        implements OrderRepository {

    private JPAOrderDAO jpaOrderDAO;

    public OrderRepositoryImpl(JPAOrderDAO jpaOrderDAO,
                               ApplicationEventPublisher eventPublisher) {
        super(jpaOrderDAO, eventPublisher);
    }

    @Override
    public OrderId nextId() {
        return orderId(UUID.randomUUID());
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return jpaOrderDAO.findByStatus(status).stream()
                .map(this::mapToDomain)
                .collect(toList());
    }

    @Override
    protected JPAOrder createEmptyJPAEntity(OrderId aggregateRootId) {
        return JPAOrder.builder().id(aggregateRootId.getValue()).build();
    }

    @Override
    protected void mapToJPAEntity(Order from, JPAOrder to) {
        to.setCustomerId(from.getCustomerId().getValue());
        to.setShoppingCartId(from.getShoppingCartId().getValue());
        to.setState(from.getState());
        to.setShipmentAddress(from.getShipmentAddress().map(this::mapToJPAAddress).orElse(null));
        to.setShipmentTrackingNumber(from.getShipmentTrackingNumber().map(ShipmentTrackingNumber::getValue).orElse(null));

        updateJPAOrderItems(from, to);
    }

    @Override
    protected Order mapToDomain(JPAOrder jpaOrder) {
        return Order.builder()
                .id(orderId(jpaOrder.getId()))
                .customerId(customerId(jpaOrder.getCustomerId()))
                .shoppingCartId(shoppingCartId(jpaOrder.getShoppingCartId()))
                .items(jpaOrder.getItems().stream()
                        .map(this::mapToDomain)
                        .collect(toList()))
                .state(jpaOrder.getState())
                .shipmentAddress(mapToDomain(jpaOrder.getShipmentAddress()))
                .shipmentTrackingNumber(jpaOrder.getShipmentTrackingNumber()
                        .map(ShipmentTrackingNumber::shipmentTrackingNumber)
                        .orElse(null))
                .build();
    }

    @Override
    protected UUID mapToJPAId(OrderId aggregateRootId) {
        return aggregateRootId.getValue();
    }

    private OrderItem mapToDomain(JPAOrderItem jpaOrderItem) {
        return OrderItem.builder()
                .productId(productId(jpaOrderItem.getProductId()))
                .quantity(jpaOrderItem.getQuantity())
                .productPrice(price(jpaOrderItem.getProductPrice()))
                .totalPrice(price(jpaOrderItem.getTotalPrice()))
                .build();
    }

    private ShipmentAddress mapToDomain(JPAAddress jpaAddress) {
        return ShipmentAddress.builder()
                .street(jpaAddress.getStreet())
                .number(jpaAddress.getNumber())
                .postalCode(jpaAddress.getPostalCode())
                .country(jpaAddress.getCountry())
                .build();
    }

    private JPAAddress mapToJPAAddress(ShipmentAddress shipmentAddress) {
        return JPAAddress.builder()
                .street(shipmentAddress.getStreet())
                .number(shipmentAddress.getNumber())
                .postalCode(shipmentAddress.getPostalCode())
                .country(shipmentAddress.getCountry())
                .build();
    }

    private void updateJPAOrderItems(Order from, JPAOrder to) {
        List<JPAOrderItem> existingItems = to.getItems();

        List<JPAOrderItem> updatedItems = new ArrayList<>();
        for (OrderItem item : from.getItems()) {
            JPAOrderItem jpaOrderItem = existingItems.stream()
                    .filter(existingItem -> matchesOrderItem(item, existingItem))
                    .findAny()
                    .orElseGet(() -> JPAOrderItem.builder()
                            .id(createJPAOrderItemId(from.getId(), item.getProductId()))
                            .build());

            mapToJPAEntity(item, jpaOrderItem);
            updatedItems.add(jpaOrderItem);
        }

        to.setItems(updatedItems);
    }

    private JPAOrderItem.JPAOrderItemId createJPAOrderItemId(OrderId orderId, ProductId productId) {
        return new JPAOrderItem.JPAOrderItemId(orderId.getValue(), productId.getValue());
    }

    private void mapToJPAEntity(OrderItem item, JPAOrderItem jpaOrderItem) {
        jpaOrderItem.setQuantity(item.getQuantity());
        jpaOrderItem.setProductPrice(item.getProductPrice().getValue());
        jpaOrderItem.setTotalPrice(item.getTotalPrice().getValue());
    }

    private boolean matchesOrderItem(OrderItem item, JPAOrderItem existingItem) {
        return item.getProductId().equals(productId(existingItem.getProductId()));
    }
}
