package be.ordina.beershop.order;

public class CreateOrderCommand {

    private final String customerId;

    public CreateOrderCommand(final String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
