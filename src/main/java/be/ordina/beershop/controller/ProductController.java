package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.product.UpdateProductStock;
import be.ordina.beershop.product.CreateProduct;
import be.ordina.beershop.product.ProductFacade;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFacade productFacade;

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody @Valid CreateProduct createProduct) {
        UUID productId = productFacade.createProduct(createProduct);
        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }

    @PatchMapping("/{id}/updateStock")
    public ResponseEntity<?> updateProductStock(@PathVariable("id") UUID productId, @RequestBody @Valid UpdateProductStock updateProductStock) {
        productFacade.updateProductStock(productId, updateProductStock);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(@PageableDefault(size = 20, sort = "id", direction = DESC) final Pageable pageable) {
        return ResponseEntity.ok().body(productRepository.findAll(pageable));
    }
}
