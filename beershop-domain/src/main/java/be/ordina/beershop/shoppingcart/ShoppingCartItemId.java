package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.domain.AbstractValueObject;
import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.product.ProductId;

import static java.util.Objects.requireNonNull;

public class ShoppingCartItemId extends AbstractValueObject implements Identifier {

    private final ShoppingCartId shoppingCartId;
    private final ProductId productId;

    private ShoppingCartItemId(ShoppingCartId shoppingCartId, ProductId productId) {
        this.shoppingCartId = requireNonNull(shoppingCartId);
        this.productId = requireNonNull(productId);
    }

    public ShoppingCartId getShoppingCartId() {
        return shoppingCartId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public static ShoppingCartItemId shoppingCartItemId(ShoppingCartId shoppingCartId, ProductId productId) {
        return new ShoppingCartItemId(shoppingCartId, productId);
    }
}
