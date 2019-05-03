package be.ordina.beershop.integrationTests.order;

class CreateOrderDTO {

    private String customerId;

    public CreateOrderDTO(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
