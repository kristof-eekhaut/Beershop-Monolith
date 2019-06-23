package be.ordina.beershop.service;

import be.ordina.beershop.order.OrderRepository;
import be.ordina.beershop.order.OrderStatus;
import be.ordina.beershop.order.ShipmentTrackingNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BeerShopService {  // TODO get rid of this service

    private static final String EVERY_NIGHT = "0 0 23 * * * *";

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeliveryService deliveryService;

    @Scheduled(cron = EVERY_NIGHT)
    public void updateShipmentStatus() {
        orderRepository.findByStatus(OrderStatus.SHIPMENT_REQUESTED)
                .forEach(order -> {
                    ShipmentTrackingNumber shipmentTrackingNumber = order.getShipmentTrackingNumber()
                            .orElseThrow(() -> new IllegalStateException("Order in status SHIPMENT_REQUESTED should have a tracking number: " + order.getId()));
                    final ShipmentResponseDto shipmentResponse = deliveryService.pollShipmentStatus(shipmentTrackingNumber);

                    if (shipmentResponse.isDelivered()) {
                        order.delivered();
                    }
                });
    }
}
