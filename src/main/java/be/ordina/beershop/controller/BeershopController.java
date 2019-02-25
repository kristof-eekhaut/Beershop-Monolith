package be.ordina.beershop.controller;

import be.ordina.beershop.domain.Order;
import be.ordina.beershop.service.BeershopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/")
public class BeershopController {

    @Autowired
    private BeershopService service;

    @PostMapping(value = "orders")
    public ResponseEntity<Void> create(@RequestBody Order order){
        service.createOrder(order);
        return ResponseEntity.created(URI.create("/orders")).build();
    }
}
