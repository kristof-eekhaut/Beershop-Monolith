package be.ordina.beershop.order;

import be.ordina.beershop.domain.matcher.BaseEntityMatcher;
import be.ordina.beershop.repository.entities.LineItem;
import org.hamcrest.Description;

import java.util.UUID;

public class LineItemMatcher extends BaseEntityMatcher<LineItem> {

    public static LineItemMatcher matchesLineItem(LineItem lineItem) {
        return new LineItemMatcher(lineItem);
    }

    private LineItemMatcher(LineItem lineItem) {
        super(lineItem);
    }

    @Override
    protected final boolean matchesSafely(LineItem other) {
        return isEqual(getProductId(this.objectToMatch), getProductId(other))
                && isEqual(this.objectToMatch.getQuantity(), other.getQuantity())
                && isEqual(this.objectToMatch.getPrice(), other.getPrice());
    }

    @Override
    protected void describeToEntity(Description description, LineItem lineItem) {
        appendField(description, "product.id", getProductId(lineItem)).appendText(",");
        appendField(description, "quantity", lineItem.getQuantity()).appendText(",");
        appendField(description, "price", lineItem.getPrice()).appendText(",");
    }

    private UUID getProductId(LineItem lineItem) {
        return lineItem.getProduct() == null ? null : lineItem.getProduct().getId();
    }
}
