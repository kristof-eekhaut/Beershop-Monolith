package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.product.CreateProduct;
import be.ordina.beershop.product.ProductFacade;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") UUID productId, @RequestBody @Valid Product product) {
        if (product.getQuantity() == 0) {
            return ResponseEntity.badRequest().body("Quantity may not be 0");
        }
        productRepository.findById(productId)
                         .ifPresent(originalProduct -> {
                             originalProduct.setQuantity(product.getQuantity());
                             productRepository.save(originalProduct);
                         });
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(@PageableDefault(size = 20, sort = "id", direction = DESC) final Pageable pageable) {
        return ResponseEntity.ok().body(productRepository.findAll(pageable));
    }
}
