package be.ordina.beershop.order;

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
        CreateOrderCommand command = new CreateOrderCommand(createOrder.getCustomerId());
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