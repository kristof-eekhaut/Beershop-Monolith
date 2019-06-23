package be.ordina.beershop.order;

import be.ordina.beershop.exception.BusinessException;
import be.ordina.beershop.product.ProductFacade;
import be.ordina.beershop.product.ReserveItemsForOrderCommand;
import be.ordina.beershop.service.DeliveryService;
import be.ordina.beershop.service.MailService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static be.ordina.beershop.order.ShipmentTrackingNumber.shipmentTrackingNumber;
import static java.util.Objects.requireNonNull;

@Component
class RequestShipmentUseCase {

    private final OrderRepository orderRepository;
    private final ProductFacade productFacade;
    private final DeliveryService deliveryService;
    private final MailService mailService;

    RequestShipmentUseCase(OrderRepository orderRepository,
                           ProductFacade productFacade,
                           DeliveryService deliveryService,
                           MailService mailService) {
        this.orderRepository = requireNonNull(orderRepository);
        this.productFacade = requireNonNull(productFacade);
        this.deliveryService = requireNonNull(deliveryService);
        this.mailService = requireNonNull(mailService);
    }

    @Transactional
    void execute(String orderIdString) {
        OrderId orderId = OrderId.fromString(orderIdString);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getState().equals(OrderStatus.PAID)) {
            throw new IllegalStateException("Order not yet paid!");
        }

        if (reserveItems(order)) {
            final ShipmentTrackingNumber shipmentTrackingNumber = shipmentTrackingNumber(deliveryService.requestShipment(order));
            order.requestShipment(shipmentTrackingNumber);
        } else {
            order.fail();
        }

        orderRepository.update(order);
        mailService.sendMail();
    }

    private boolean reserveItems(final Order order) {
        ReserveItemsForOrderCommand.Builder commandBuilder = ReserveItemsForOrderCommand.builder()
                .orderId(order.getId().toString());

        order.getItems()
                .forEach(orderItem -> commandBuilder.itemToReserve(orderItem.getProductId().toString(), orderItem.getQuantity()));

        try {
            productFacade.reserveItemsForOrder(commandBuilder.build());
        } catch (BusinessException exception) {
            return false;
        }

        return true;
    }
}