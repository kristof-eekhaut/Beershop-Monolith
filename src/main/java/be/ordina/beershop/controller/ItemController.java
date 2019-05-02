package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.ProductRepository;
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
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ProductRepository repository;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Product item) {
        item.setId(UUID.randomUUID());
        item.setCreatedOn(LocalDateTime.now());
        Product savedItem = repository.save(item);
        return ResponseEntity.created(URI.create("/items/" + savedItem.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getItem(@PathVariable("id") UUID id) {
        Optional<Product> item = repository.findById(id);
        return item.map(it -> ResponseEntity.ok().body(it)).get();
    }
}
