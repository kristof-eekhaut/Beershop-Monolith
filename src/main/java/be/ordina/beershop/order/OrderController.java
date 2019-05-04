package be.ordina.beershop.order;

import be.ordina.beershop.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

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
        UUID orderId = orderFacade.createOrder(createOrder);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID id) {
        return orderFacade.getOrder(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<Void> payOrder(@PathVariable("id") UUID id) {
        orderFacade.payOrder(id);
        return ResponseEntity.ok().build();
    }
}