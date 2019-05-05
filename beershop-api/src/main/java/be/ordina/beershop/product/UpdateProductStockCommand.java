package be.ordina.beershop.product;

public class UpdateProductStockCommand {

    private final String productId;
    private final int quantity;

    public UpdateProductStockCommand(final String productId,
                                     final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
