package be.ordina.beershop.service;

import be.ordina.beershop.order.Order;
import be.ordina.beershop.order.ShipmentTrackingNumber;
import be.ordina.beershop.product.JPAProductDAO;
import be.ordina.beershop.repository.entities.JPAProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyMap;

@Service
public class DeliveryService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JPAProductDAO jpaProductDAO;

    public String requestShipment(final Order order) {
        final BigDecimal totalWeight = order.getItems().stream()
                .map(orderItem -> {
                    JPAProduct product = jpaProductDAO.findById(orderItem.getProductId().getValue())
                            .orElseThrow(() -> new IllegalStateException("product not found: " + orderItem.getProductId()));
                    return product.getWeight().getAmountInGrams()
                            .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                })
                .reduce(ZERO, BigDecimal::add);
        final AddressDto shipmentAddress = order.getShipmentAddress()
                .map(address -> new AddressDto(address.getStreet(), address.getNumber(), address.getCountry(), address.getPostalCode()))
                .orElseThrow(() -> new IllegalStateException("Can not do shipment without address."));
        final ShipmentRequestDto shipmentRequest = new ShipmentRequestDto(shipmentAddress, totalWeight);
        final ResponseEntity<ShipmentResponseDto> response = restTemplate.postForEntity(
                "http://example.be/createShipment", shipmentRequest, ShipmentResponseDto.class, emptyMap());

        return response.getBody().getShipmentId();
    }

    public ShipmentResponseDto pollShipmentStatus(final ShipmentTrackingNumber shipmentId) {
        final ResponseEntity<ShipmentResponseDto> response = restTemplate.getForEntity(
                "http://example.be/shipment/" + shipmentId.getValue(), ShipmentResponseDto.class);
        return response.getBody();
    }
}
