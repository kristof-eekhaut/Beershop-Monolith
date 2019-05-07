package be.ordina.beershop.order;

import be.ordina.beershop.domain.matcher.BaseEntityMatcher;
import be.ordina.beershop.repository.entities.JPAShoppingCartItem;
import org.hamcrest.Description;

public class JPAShoppingCartItemMatcher extends BaseEntityMatcher<JPAShoppingCartItem> {

    public static JPAShoppingCartItemMatcher matchesLineItem(JPAShoppingCartItem shoppingCartItem) {
        return new JPAShoppingCartItemMatcher(shoppingCartItem);
    }

    private JPAShoppingCartItemMatcher(JPAShoppingCartItem shoppingCartItem) {
        super(shoppingCartItem);
    }

    @Override
    protected final boolean matchesSafely(JPAShoppingCartItem other) {
        return isEqual(this.objectToMatch.getProductId(), other.getProductId())
                && isEqual(this.objectToMatch.getQuantity(), other.getQuantity())
                && isEqual(this.objectToMatch.getProductPrice(), other.getProductPrice());
    }

    @Override
    protected void describeToEntity(Description description, JPAShoppingCartItem shoppingCartItem) {
        appendField(description, "productId", shoppingCartItem.getProductId()).appendText(",");
        appendField(description, "quantity", shoppingCartItem.getQuantity()).appendText(",");
        appendField(description, "productPrice", shoppingCartItem.getProductPrice()).appendText(",");
    }
}
