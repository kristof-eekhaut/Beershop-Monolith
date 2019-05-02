package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Discount;
import be.ordina.beershop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<Void> createDiscount(@RequestBody Discount discount) {
        productRepository.findById(discount.getProductId())
                         .ifPresent(product -> product.addDiscount(discount));
        return ResponseEntity.ok().build();
    }

}
