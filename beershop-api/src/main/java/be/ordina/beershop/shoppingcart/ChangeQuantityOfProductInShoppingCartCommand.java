package be.ordina.beershop.shoppingcart;

public class ChangeQuantityOfProductInShoppingCartCommand {

    private final String productId;
    private final int quantity;

    public ChangeQuantityOfProductInShoppingCartCommand(final String productId,
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
