package be.ordina.beershop.service;

import be.ordina.beershop.domain.Order;
import be.ordina.beershop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeershopService {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(Order order){
        orderRepository.save(order);
    }
}
