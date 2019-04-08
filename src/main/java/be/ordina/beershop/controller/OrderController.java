package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.OrderStatus;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.OrderRepository;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
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
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Order order) {

        final Optional<Customer> maybeCustomer = customerRepository.findById(order.getCustomer().getId());
        if(!maybeCustomer.isPresent()) {
            return ResponseEntity.badRequest().body("Unknown customer");
        }

        final boolean allProductsFound = order.getLineItems()
                               .stream()
                               .map(lineItem -> productRepository.findById(lineItem.getProduct().getId()))
                               .allMatch(Optional::isPresent);
        if(!allProductsFound) {
            return ResponseEntity.badRequest().body("Unknown product");
        }

        order.getLineItems()
             .forEach(lineItem -> {
                 final Product product = productRepository.getOne(lineItem.getProduct().getId());
                 final BigDecimal linePriceTotal = product.getPrice().multiply(BigDecimal.valueOf(lineItem.getQuantity()));
                 lineItem.setId(UUID.randomUUID());
                 lineItem.setProduct(product);
                 lineItem.setPrice(linePriceTotal);
             });

        order.setId(UUID.randomUUID());
        order.setCustomer(maybeCustomer.get());
        order.setState(OrderStatus.ORDER_CREATED);
        order.setCreatedOn(LocalDateTime.now());
        order.setAddress(maybeCustomer.get().getAddress());
        repository.save(order);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).build();
    }
}
