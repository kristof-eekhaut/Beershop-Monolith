package be.ordina.beershop.orders.domain.entities;

import be.ordina.beershop.orders.domain.CreateOrderData;

public class Order {

    private String number;

    public Order(CreateOrderData orderCreationServiceData) {
        this.number = orderCreationServiceData.getNumber();
    }

    public String getNumber() {
        return number;
    }
}
