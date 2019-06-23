package be.ordina.beershop.order;

import be.ordina.beershop.domain.matcher.BaseEntityMatcher;
import be.ordina.beershop.repository.entities.JPAOrderItem;
import org.hamcrest.Description;

public class JPAOrderItemMatcher extends BaseEntityMatcher<JPAOrderItem> {

    public static JPAOrderItemMatcher matchesOrderItem(JPAOrderItem orderItem) {
        return new JPAOrderItemMatcher(orderItem);
    }

    private JPAOrderItemMatcher(JPAOrderItem orderItem) {
        super(orderItem);
    }

    @Override
    protected final boolean matchesSafely(JPAOrderItem other) {
        return isEqual(this.objectToMatch.getProductId(), other.getProductId())
                && isEqual(this.objectToMatch.getQuantity(), other.getQuantity())
                && isEqual(this.objectToMatch.getProductPrice(), other.getProductPrice());
    }

    @Override
    protected void describeToEntity(Description description, JPAOrderItem orderItem) {
        appendField(description, "productId", orderItem.getProductId()).appendText(",");
        appendField(description, "quantity", orderItem.getQuantity()).appendText(",");
        appendField(description, "productPrice", orderItem.getProductPrice()).appendText(",");
    }
}
