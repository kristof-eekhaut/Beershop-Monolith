package be.ordina.beershop.product;

import be.ordina.beershop.product.dto.WeightDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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
        WeightDTO weightDTO = null;
        if (createProduct.getWeight() != null) {
            weightDTO = new WeightDTO(createProduct.getWeight().getAmount(), createProduct.getWeight().getUnit());
        }

        CreateProductCommand command = CreateProductCommand.builder()
                .name(createProduct.getName())
                .alcoholPercentage(createProduct.getAlcoholPercentage())
                .weight(weightDTO)
                .quantity(createProduct.getQuantity())
                .price(createProduct.getPrice())
                .build();
        String productId = productFacade.createProduct(command);

        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }

    @PatchMapping("/{id}/update-stock")
    public ResponseEntity<?> updateProductStock(@PathVariable("id") String productId,
                                                @RequestBody @Valid UpdateProductStock updateProductStock) {
        UpdateProductStockCommand command = new UpdateProductStockCommand(
                productId,
                updateProductStock.getQuantity()
        );
        productFacade.updateProductStock(command);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductView>> getAllProducts(@PageableDefault(size = 20, sort = "id", direction = DESC) final Pageable pageable) {
        Page<ProductView> products = productFacade.getAllProducts(pageable);

        return ResponseEntity.ok().body(products);
    }
}
