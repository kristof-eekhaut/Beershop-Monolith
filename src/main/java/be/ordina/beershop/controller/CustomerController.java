package be.ordina.beershop.controller;

import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.ProductRepository;
import be.ordina.beershop.service.BeerShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private BeerShopService beerShopService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/{customerId}/shoppingCart/items")
    public ResponseEntity<?> addItemToShoppingCart(@PathVariable UUID customerId, @RequestBody LineItem lineItem) {
        if (customerRepository.findById(customerId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final boolean productExists = productRepository.findById(lineItem.getProduct().getId()).isPresent();
        if(!productExists) {
            return ResponseEntity.badRequest().body("Unknown product");
        }
        beerShopService.createItemInShoppingCart(customerId, lineItem);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{customerId}/shoppingCart/items/{lineItemId}")
    public ResponseEntity<Void> updateItemInShoppingCart(@PathVariable UUID customerId, @PathVariable String lineItemId, @RequestBody LineItem lineItem) {
        if (customerRepository.findById(customerId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        beerShopService.updateLineInShoppingCart(customerId, lineItem);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}/shoppingCart/items/{lineItemId}")
    public ResponseEntity<Void> deleteItemFromShoppingCart(@PathVariable UUID customerId, @PathVariable UUID lineItemId) {
        if (customerRepository.findById(customerId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        beerShopService.deleteLineInShoppingCart(customerId, lineItemId);
        return ResponseEntity.ok().build();
    }
}
