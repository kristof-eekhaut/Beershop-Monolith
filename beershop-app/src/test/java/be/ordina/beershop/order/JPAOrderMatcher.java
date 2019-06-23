package be.ordina.beershop.order;

import be.ordina.beershop.domain.matcher.BaseEntityMatcher;
import be.ordina.beershop.repository.entities.JPAOrder;
import be.ordina.beershop.repository.entities.JPAOrderItem;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class JPAOrderMatcher extends BaseEntityMatcher<JPAOrder> {

    private final Matcher<? super List<JPAOrderItem>> orderItems;

    public static JPAOrderMatcher matchesOrder(JPAOrder order) {
        return new JPAOrderMatcher(order);
    }

    private JPAOrderMatcher(JPAOrder order) {
        super(order);
        this.orderItems = matchingOrderItems(order);
    }

    private Matcher<? super List<JPAOrderItem>> matchingOrderItems(JPAOrder order) {
        List<Matcher<? super JPAOrderItem>> discountsMatchers = order.getItems().stream()
                .map(JPAOrderItemMatcher::matchesOrderItem)
                .collect(Collectors.toList());
        return containsInAnyOrder(discountsMatchers);
    }

    @Override
    protected final boolean matchesSafely(JPAOrder other) {
        return isEqual(this.objectToMatch.getCustomerId(), other.getCustomerId())
                && isEqual(this.objectToMatch.getShoppingCartId(), other.getShoppingCartId())
//                && isEqual(this.objectToMatch.getCreatedOn(), other.getCreatedOn())  TODO:  inject Clock to be able to test with FixedClock
                && isEqual(this.objectToMatch.getState(), other.getState())
                && isEqual(this.objectToMatch.getShipmentAddress(), other.getShipmentAddress())
                && isEqual(this.objectToMatch.getShipmentTrackingNumber(), other.getShipmentTrackingNumber())
                && this.orderItems.matches(other.getItems());
    }

    @Override
    protected void describeToEntity(Description description, JPAOrder order) {
        appendField(description, "customerId", order.getCustomerId()).appendText(",");
        appendField(description, "shoppingCartId", order.getShoppingCartId()).appendText(",");
//        appendField(description, "createdOn", order.getCreatedOn()).appendText(",");
        appendField(description, "state", order.getState()).appendText(",");
        appendField(description, "shipmentAddress", order.getShipmentAddress()).appendText(",");
        appendField(description, "shipmentTrackingNumber", order.getShipmentTrackingNumber()).appendText(",");
        appendDescriptionOf(description, "orderItems", matchingOrderItems(order));
    }
}
