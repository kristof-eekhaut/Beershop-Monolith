package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.service.BeerShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private BeerShopService beerShopService;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShoppingCartFacade shoppingCartFacade;

    @PostMapping("/{customerId}/shopping-cart/line-items")
    public ResponseEntity<?> addProductToShoppingCart(@PathVariable UUID customerId, @RequestBody @Valid AddProductToShoppingCart addProductToShoppingCart) {
        shoppingCartFacade.addProduct(customerId, addProductToShoppingCart);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{customerId}/shopping-cart/line-items/{lineItemId}")
    public ResponseEntity<Void> updateItemInShoppingCart(@PathVariable UUID customerId, @PathVariable String lineItemId, @RequestBody LineItem lineItem) {
        if (customerRepository.findById(customerId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        beerShopService.updateLineInShoppingCart(customerId, lineItem);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}/shopping-cart/line-items/{productId}")
    public ResponseEntity<Void> removeProductFromShoppingCart(@PathVariable UUID customerId, @PathVariable UUID productId) {
        shoppingCartFacade.removeProduct(customerId, productId);
        return ResponseEntity.ok().build();
    }
}
