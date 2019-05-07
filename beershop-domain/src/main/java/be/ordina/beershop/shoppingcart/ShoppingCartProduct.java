package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.common.Price;
import be.ordina.beershop.domain.AbstractValueObject;
import be.ordina.beershop.product.ProductId;

import static java.util.Objects.requireNonNull;

public class ShoppingCartProduct extends AbstractValueObject {

    private ProductId productId;
    private Price discountedPrice;

    private ShoppingCartProduct(ProductId productId, Price discountedPrice) {
        this.productId = requireNonNull(productId);
        this.discountedPrice = requireNonNull(discountedPrice);
    }

    public ProductId getProductId() {
        return productId;
    }

    public Price getDiscountedPrice() {
        return discountedPrice;
    }

    public static ShoppingCartProduct shoppingCartProduct(ProductId productId, Price discountedPrice) {
        return new ShoppingCartProduct(productId, discountedPrice);
    }
}
