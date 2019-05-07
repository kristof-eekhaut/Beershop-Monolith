package be.ordina.beershop.service;

import be.ordina.beershop.repository.JPAProductDAO;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper objectMapper;
    @Autowired
    private JPAProductDAO jpaProductDAO;

    public String requestShipment(final Order order) {
        final BigDecimal totalWeight = order
                .getLineItems()
                .stream()
                .map(lineItem -> {
                    JPAProduct product = jpaProductDAO.findById(lineItem.getProductId())
                            .orElseThrow(() -> new IllegalStateException("product not found: " + lineItem.getProductId()));
                    return product.getWeight().getAmountInGrams()
                            .multiply(BigDecimal.valueOf(lineItem.getQuantity()));
                })
                .reduce(ZERO, BigDecimal::add);
        final ShipmentRequestDto shipmentRequest = new ShipmentRequestDto(order.getAddress(), totalWeight);
        final ResponseEntity<ShipmentResponseDto> response = restTemplate.postForEntity(
                "http://example.be/createShipment", shipmentRequest, ShipmentResponseDto.class, emptyMap());

        return response.getBody().getShipmentId();
    }

    public ShipmentResponseDto pollShipmentStatus(final String shipmentId) {
        final ResponseEntity<ShipmentResponseDto> response = restTemplate.getForEntity(
                "http://example.be/shipment/" + shipmentId, ShipmentResponseDto.class);
        return response.getBody();
    }
}
