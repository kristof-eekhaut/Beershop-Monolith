package be.ordina.beershop.service;

import be.ordina.beershop.controller.PaymentResource;
import be.ordina.beershop.controller.ShipmentResource;
import be.ordina.beershop.domain.*;
import be.ordina.beershop.order.CreateOrder;
import be.ordina.beershop.order.CustomerNotFoundException;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.OrderRepository;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;
import static java.math.RoundingMode.UNNECESSARY;
import static java.time.ZonedDateTime.now;

@Service
@Transactional
public class BeerShopService {

    private static final String EVERY_NIGHT = "0 0 23 * * * *";
    private static final BigDecimal LEGAL_DRINKING_ALCOHOL_LIMIT = BigDecimal.valueOf(15);
    private static final int LEGAL_DRINKING_AGE = 18;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private CustomerRepository customerRepository;

    public Order createOrder(final CreateOrder createOrder) {
        final Customer customer = customerRepository.findById(createOrder.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(createOrder.getCustomerId()));
        final List<LineItem> lineItems = customer.getShoppingCart().getLineItems();

        final Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCustomer(customer);
        order.setState(OrderStatus.CREATED);
        order.setCreatedOn(LocalDateTime.now());
        order.setAddress(customer.getAddress());
        order.setLineItems(lineItems);
        final Order savedOrder = orderRepository.save(order);

        order.getCustomer().getShoppingCart().clear();
        return savedOrder;
    }


    @Transactional
    public void requestShipment(final ShipmentResource shipmentResource) {
        final Optional<Order> maybeOrder = orderRepository.findById(shipmentResource.getOrderId());
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

    public void payOrder(final PaymentResource paymentResource) {
        orderRepository.findById(paymentResource.getOrderId())
                       .ifPresent(order -> updateOrderStatus(order, OrderStatus.PAID));
    }

    private void updateStock(final Order order) {
        order.getLineItems()
             .forEach(lineItem -> {
                 final int originalQuantity = lineItem.getProduct().getQuantity();
                 final int newQuantity = originalQuantity - lineItem.getQuantity();
                 lineItem.getProduct().setQuantity(newQuantity);
             });
    }

    private void updateOrderStatus(final Order order, final OrderStatus newOrderStatus) {
        order.setState(newOrderStatus);
        orderRepository.save(order);
    }

    private boolean allLineItemsHaveEnoughStock(final Order order) {
        return order.getLineItems()
                    .stream()
                    .allMatch(lineItem -> lineItem.getQuantity() <= lineItem.getProduct().getQuantity());
    }

    public void createItemInShoppingCart(final UUID customerId, final LineItem lineItem) {
        final Customer customer = customerRepository.findById(customerId).get();
        initializeLineItem(lineItem);
        if (customerIsOldEnoughForProduct(lineItem, customer)) {
            throw new RuntimeException("No underage drinking allowed");
        }
        final ShoppingCart shoppingCart = customer.getShoppingCart();
        shoppingCart.addLineItem(lineItem);
        customerRepository.save(customer);
    }

    private boolean customerIsOldEnoughForProduct(final LineItem lineItem, final Customer customer) {
        final boolean alcoholPercentageAboveThreshold = lineItem.getProduct().getAlcoholPercentage().compareTo( LEGAL_DRINKING_ALCOHOL_LIMIT) == 1;
        final boolean customerIsUnderaged = Period.between(customer.getBirthDate(), LocalDate.now()).getYears() < LEGAL_DRINKING_AGE;
        return alcoholPercentageAboveThreshold && customerIsUnderaged;
    }

    private void initializeLineItem(final LineItem lineItem) {
        if (lineItem.getQuantity() == 0) {
            throw new RuntimeException("Quantity should not be 0");
        }
        final Product product = productRepository.getOne(lineItem.getProduct().getId());
        final BigDecimal price = calculateProductPrice(product);
        final BigDecimal linePriceTotal = price.multiply(BigDecimal.valueOf(lineItem.getQuantity()));
        lineItem.setId(UUID.randomUUID());
        lineItem.setProduct(product);
        lineItem.setPrice(linePriceTotal);
    }

    private BigDecimal calculateProductPrice(final Product product) {
        final Discount activeDiscount = product
                .getDiscounts()
                .stream()
                .filter(discount -> discount.getStartDate().isBefore(now()) && discount.getEndDate().isAfter(now()))
                .findFirst()
                .orElse(Discount.NONE);
        final BigDecimal discountPercentage = activeDiscount.getPercentage().divide(BigDecimal.valueOf(100),
                UNNECESSARY);
        final BigDecimal discountAmount = product.getPrice().multiply(discountPercentage);
        return product.getPrice().subtract(discountAmount).setScale(2, HALF_UP);
    }

    public void updateLineInShoppingCart(final UUID customerId, final LineItem lineItem) {
        final Customer customer = customerRepository.getOne(customerId);
        initializeLineItem(lineItem);
        customer.getShoppingCart().updateLineItem(lineItem);
        customerRepository.save(customer);
    }

    public void deleteLineInShoppingCart(final UUID customerId, final UUID lineItemId) {
        final Customer customer = customerRepository.getOne(customerId);
        customer.getShoppingCart().deleteLine(lineItemId);
        customerRepository.save(customer);
    }
}
