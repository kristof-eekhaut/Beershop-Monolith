package be.ordina.beershop.shoppingcart;

public class AddProductToShoppingCartCommand {

    private final String productId;
    private final int quantity;

    public AddProductToShoppingCartCommand(final String productId,
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
