package be.ordina.beershop.service;

import be.ordina.beershop.order.CreateOrderCommand;
import be.ordina.beershop.order.CustomerNotFoundException;
import be.ordina.beershop.order.OrderNotFoundException;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.JPAProductDAO;
import be.ordina.beershop.repository.OrderRepository;
import be.ordina.beershop.repository.entities.*;
import be.ordina.beershop.shoppingcart.JPAShoppingCartDAO;
import be.ordina.beershop.shoppingcart.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BeerShopService {

    private static final String EVERY_NIGHT = "0 0 23 * * * *";
    private static final BigDecimal LEGAL_DRINKING_ALCOHOL_LIMIT = BigDecimal.valueOf(15);
    private static final int LEGAL_DRINKING_AGE = 18;

    @Autowired
    private JPAProductDAO productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JPAShoppingCartDAO jpaShoppingCartDAO;

    public Order createOrder(final CreateOrderCommand createOrderCommand) {
        UUID customerId = UUID.fromString(createOrderCommand.getCustomerId());
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        final JPAShoppingCart jpaShoppingCart = jpaShoppingCartDAO.findByCustomerId(customerId)
                .orElse(JPAShoppingCart.builder().id(UUID.randomUUID()).customerId(customerId).build());
        final List<JPAShoppingCartItem> lineItems = jpaShoppingCart.getItems();

        final Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCustomer(customer);
        order.setState(OrderStatus.CREATED);
        order.setCreatedOn(LocalDateTime.now());
        order.setAddress(customer.getAddress());
        order.setLineItems(lineItems);
        final Order savedOrder = orderRepository.save(order);

        jpaShoppingCart.setItems(new ArrayList<>());
        return savedOrder;
    }


    @Transactional
    public void requestShipment(final UUID orderId) {
        final Optional<Order> maybeOrder = orderRepository.findById(orderId);
        maybeOrder.ifPresent(order -> {

            if (order.getState() != OrderStatus.PAID) {
                throw new RuntimeException("Order cannot be shipped if it's not paid");
            }

            if (!allLineItemsHaveEnoughStock(order)) {
                mailService.sendMail();
                updateOrderStatus(order, OrderStatus.ORDER_FAILED);
                return;
            }
            updateStock(order);
            final String shipmentId = deliveryService.requestShipment(order);
            order.setShipmentId(shipmentId);
            orderRepository.save(order);
            updateOrderStatus(order, OrderStatus.SHIPMENT_REQUESTED);
            mailService.sendMail();
        });
    }

    @Scheduled(cron = EVERY_NIGHT)
    public void updateShipmentStatus() {
        orderRepository.findByStatus(OrderStatus.SHIPMENT_REQUESTED)
                       .forEach(order -> {
                           final ShipmentResponseDto shipmentResponse = deliveryService
                                   .pollShipmentStatus(order.getShipmentId());

                           if (shipmentResponse.isDelivered()) {
                               updateOrderStatus(order, OrderStatus.DELIVERED);
                           }
                       });
    }

    public void payOrder(final UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        updateOrderStatus(order, OrderStatus.PAID);
    }

    private void updateStock(final Order order) {
        order.getLineItems()
             .forEach(lineItem -> {
                 JPAProduct product = productRepository.findById(lineItem.getProductId())
                         .orElseThrow(() -> new ProductNotFoundException(ProductId.productId(lineItem.getProductId())));

                 final int originalQuantity = product.getQuantity();
                 final int newQuantity = originalQuantity - lineItem.getQuantity();
                 product.setQuantity(newQuantity);
             });
    }

    private void updateOrderStatus(final Order order, final OrderStatus newOrderStatus) {
        order.setState(newOrderStatus);
        orderRepository.save(order);
    }

    private boolean allLineItemsHaveEnoughStock(final Order order) {
        return order.getLineItems()
                    .stream()
                    .allMatch(lineItem -> {
                        JPAProduct product = productRepository.findById(lineItem.getProductId())
                                .orElseThrow(() -> new ProductNotFoundException(ProductId.productId(lineItem.getProductId())));
                        return lineItem.getQuantity() <= product.getQuantity();
                    });
    }
}
