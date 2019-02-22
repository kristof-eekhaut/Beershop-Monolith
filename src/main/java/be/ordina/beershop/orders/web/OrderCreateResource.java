package be.ordina.beershop.orders.web;

import be.ordina.beershop.orders.domain.CreateOrderData;

public class OrderCreateResource implements CreateOrderData {

    private String number;

    public OrderCreateResource() {
    }

    public OrderCreateResource(String number) {
        this.number = number;
    }

    @Override
    public String getNumber() {
        return number;
    }
}
