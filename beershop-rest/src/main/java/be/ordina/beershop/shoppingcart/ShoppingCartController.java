package be.ordina.beershop.shoppingcart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/customers/{customerId}/shopping-cart")
class ShoppingCartController {

    private final ShoppingCartFacade shoppingCartFacade;

    ShoppingCartController(ShoppingCartFacade shoppingCartFacade) {
        this.shoppingCartFacade = requireNonNull(shoppingCartFacade);
    }

    @GetMapping()
    public ResponseEntity<ShoppingCartView> getShoppingCart(@PathVariable String customerId) {
        return shoppingCartFacade.getShoppingCart(customerId)
                .map(shoppingCart -> ResponseEntity.ok().body(shoppingCart))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/add-product")
    public ResponseEntity<?> addProductToShoppingCart(@PathVariable String customerId, @RequestBody @Valid AddProductToShoppingCart addProductToShoppingCart) {
        AddProductToShoppingCartCommand command = new AddProductToShoppingCartCommand(
                addProductToShoppingCart.getProductId(),
                addProductToShoppingCart.getQuantity()
        );
        shoppingCartFacade.addProduct(customerId, command);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-quantity")
    public ResponseEntity<Void> updateItemInShoppingCart(@PathVariable String customerId, @RequestBody @Valid ChangeQuantityOfProductInShoppingCart changeQuantityOfProductInShoppingCart) {
        ChangeQuantityOfProductInShoppingCartCommand command = new ChangeQuantityOfProductInShoppingCartCommand(
                changeQuantityOfProductInShoppingCart.getProductId(),
                changeQuantityOfProductInShoppingCart.getQuantity()
        );
        shoppingCartFacade.changeQuantityOfProduct(customerId, command);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/remove-product/{productId}")
    public ResponseEntity<Void> removeProductFromShoppingCart(@PathVariable String customerId, @PathVariable String productId) {
        shoppingCartFacade.removeProduct(customerId, productId);
        return ResponseEntity.ok().build();
    }
}
