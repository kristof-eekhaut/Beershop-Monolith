package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.OrderRepository;
import be.ordina.beershop.service.BeerShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BeerShopService beerShopService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderResource orderResource) {

        final Optional<Customer> maybeCustomer = customerRepository.findById(orderResource.getCustomerId());
        if(!maybeCustomer.isPresent()) {
            return ResponseEntity.badRequest().body("Unknown customer");
        }

        final Order order = beerShopService.createOrder(orderResource);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID id) {
        Optional<Order> order = repository.findById(id);
        return order.map(or -> ResponseEntity.ok().body(or)).get();
    }
}