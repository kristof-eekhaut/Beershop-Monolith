package be.ordina.beershop.integrationTests.shoppingcart;

import java.util.UUID;

class ChangeQuantityOfItemInShoppingCartDTO {

    private String productId;
    private int quantity;

    ChangeQuantityOfItemInShoppingCartDTO(UUID productId, int quantity) {
        this.productId = productId.toString();
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
