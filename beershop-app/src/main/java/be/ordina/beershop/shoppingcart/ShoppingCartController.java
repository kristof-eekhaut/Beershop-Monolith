package be.ordina.beershop.shoppingcart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/customers/{customerId}/shopping-cart")
class ShoppingCartController {

    private final ShoppingCartFacade shoppingCartFacade;

    ShoppingCartController(ShoppingCartFacade shoppingCartFacade) {
        this.shoppingCartFacade = requireNonNull(shoppingCartFacade);
    }

    @PatchMapping("/add-product")
    public ResponseEntity<?> addProductToShoppingCart(@PathVariable UUID customerId, @RequestBody @Valid AddProductToShoppingCart addProductToShoppingCart) {
        shoppingCartFacade.addProduct(customerId, addProductToShoppingCart);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-quantity")
    public ResponseEntity<Void> updateItemInShoppingCart(@PathVariable UUID customerId, @RequestBody @Valid ChangeQuantityOfProductInShoppingCart changeQuantityOfProductInShoppingCart) {
        shoppingCartFacade.changeQuantityOfProduct(customerId, changeQuantityOfProductInShoppingCart);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/remove-product/{productId}")
    public ResponseEntity<Void> removeProductFromShoppingCart(@PathVariable UUID customerId, @PathVariable UUID productId) {
        shoppingCartFacade.removeProduct(customerId, productId);
        return ResponseEntity.ok().build();
    }
}
