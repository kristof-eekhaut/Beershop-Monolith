package be.ordina.beershop.order;

import be.ordina.beershop.domain.Order;
import be.ordina.beershop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateOrder createOrder) {
        UUID orderId = orderFacade.createOrder(createOrder);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID id) {
        Optional<Order> order = repository.findById(id);
        return order.map(or -> ResponseEntity.ok().body(or)).get();
    }
}