package be.ordina.beershop.integrationTests.order;

public class RequestShipmentDTO {

    private String orderId;

    public RequestShipmentDTO(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
