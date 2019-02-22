package be.ordina.beershop.orders.web;

import be.ordina.beershop.orders.domain.usecases.CreateOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderController(final CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Void> create(@RequestBody OrderCreateResource resource){
        createOrderUseCase.createOrder(resource);
        return ResponseEntity.ok().build();
    }
}
