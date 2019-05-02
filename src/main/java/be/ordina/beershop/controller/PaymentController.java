package be.ordina.beershop.controller;

import be.ordina.beershop.service.BeerShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private BeerShopService beerShopService;

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody PaymentResource paymentResource) {
        beerShopService.payOrder(paymentResource);
        return ResponseEntity.ok().build();
    }

}
