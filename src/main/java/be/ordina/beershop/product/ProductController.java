package be.ordina.beershop.product;

import be.ordina.beershop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/products")
class ProductController {

    private final ProductFacade productFacade;

    ProductController(ProductFacade productFacade) {
        this.productFacade = requireNonNull(productFacade);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody @Valid CreateProduct createProduct) {
        UUID productId = productFacade.createProduct(createProduct);
        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }

    @PatchMapping("/{id}/updateStock")
    public ResponseEntity<?> updateProductStock(@PathVariable("id") UUID productId,
                                                @RequestBody @Valid UpdateProductStock updateProductStock) {
        productFacade.updateProductStock(productId, updateProductStock);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(@PageableDefault(size = 20, sort = "id", direction = DESC) final Pageable pageable) {
        Page<Product> products = productFacade.getProducts(pageable);
        return ResponseEntity.ok().body(products);
    }
}
