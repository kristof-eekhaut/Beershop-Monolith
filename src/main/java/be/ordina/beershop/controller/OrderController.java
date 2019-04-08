package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.OrderStatus;
import be.ordina.beershop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository repository;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        order.setId(UUID.randomUUID());
        order.setState(OrderStatus.ORDER_CREATED);
        order.setCreatedOn(LocalDateTime.now());
        repository.save(order);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID id) {
        Optional<Order> order = repository.findById(id);
        return order.map(or -> ResponseEntity.ok().body(or)).get();
    }
}