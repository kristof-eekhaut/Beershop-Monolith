package be.ordina.beershop.discount;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/discounts")
class DiscountController {

    private DiscountFacade discountFacade;

    DiscountController(DiscountFacade discountFacade) {
        this.discountFacade = requireNonNull(discountFacade);
    }

    @PostMapping
    public ResponseEntity<Void> createDiscount(@RequestBody @Valid CreateDiscount createDiscount) {
        discountFacade.createDiscount(createDiscount);
        return ResponseEntity.ok().build();
    }

}
