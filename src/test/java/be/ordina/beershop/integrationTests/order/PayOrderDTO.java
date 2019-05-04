package be.ordina.beershop.integrationTests.order;

class PayOrderDTO {

    private String orderId;

    public PayOrderDTO(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
