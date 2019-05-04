package be.ordina.beershop.integrationTests.shoppingcart;

import java.util.UUID;

class ChangeQuantityOfItemInShoppingCartDTO {

    private Product product;
    private int quantity;

    ChangeQuantityOfItemInShoppingCartDTO(UUID productId, int quantity) {
        this.product = new Product(productId);
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    class Product {
        private UUID id;

        private Product(UUID id) {
            this.id = id;
        }

        public UUID getId() {
            return id;
        }
    }
}
