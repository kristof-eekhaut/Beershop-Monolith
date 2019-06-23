package be.ordina.beershop.order;

import be.ordina.beershop.order.dto.ShipmentAddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/orders")
class OrderController {

    private final OrderFacade orderFacade;

    OrderController(OrderFacade orderFacade) {
        this.orderFacade = requireNonNull(orderFacade);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateOrder createOrder) {
        ShipmentAddressDTO shipmentAddress = createOrder.getShipmentAddress() == null ? null :ShipmentAddressDTO.builder()
                .street(createOrder.getShipmentAddress().getStreet())
                .number(createOrder.getShipmentAddress().getNumber())
                .country(createOrder.getShipmentAddress().getCountry())
                .postalCode(createOrder.getShipmentAddress().getPostalCode())
                .build();
        CreateOrderCommand command = new CreateOrderCommand(createOrder.getCustomerId(), shipmentAddress);
        String orderId = orderFacade.createOrder(command);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderView> getOrder(@PathVariable("id") String orderId) {
        return orderFacade.getOrder(orderId)
                .map(order -> ResponseEntity.ok().body(order))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<Void> payOrder(@PathVariable("id") String orderId) {
        orderFacade.payOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/requestShipment")
    public ResponseEntity<Void> requestShipment(@PathVariable("id") String orderId) {
        orderFacade.requestShipment(orderId);
        return ResponseEntity.ok().build();
    }
}