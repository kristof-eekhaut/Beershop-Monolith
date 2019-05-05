package be.ordina.beershop.order;

import be.ordina.beershop.common.matcher.BaseEntityMatcher;
import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Order;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class OrderMatcher extends BaseEntityMatcher<Order> {

    private final Matcher<? super List<LineItem>> lineItems;

    public static OrderMatcher matchesOrder(Order order) {
        return new OrderMatcher(order);
    }

    private OrderMatcher(Order order) {
        super(order);
        this.lineItems = matchingLineItems(order);
    }

    private Matcher<? super List<LineItem>> matchingLineItems(Order order) {
        List<Matcher<? super LineItem>> discountsMatchers = order.getLineItems().stream()
                .map(LineItemMatcher::matchesLineItem)
                .collect(Collectors.toList());
        return containsInAnyOrder(discountsMatchers);
    }

    @Override
    protected final boolean matchesSafely(Order other) {
        return isEqual(getCustomerId(this.objectToMatch), getCustomerId(other))
                && isEqual(this.objectToMatch.getAddress(), other.getAddress())
//                && isEqual(this.objectToMatch.getCreatedOn(), other.getCreatedOn())  TODO:  inject Clock to be able to test with FixedClock
                && isEqual(this.objectToMatch.getState(), other.getState())
                && isEqual(this.objectToMatch.getShipmentId(), other.getShipmentId())
                && this.lineItems.matches(other.getLineItems());
    }

    @Override
    protected void describeToEntity(Description description, Order order) {
        appendField(description, "customer.id", getCustomerId(order)).appendText(",");
        appendField(description, "address", order.getAddress()).appendText(",");
//        appendField(description, "createdOn", order.getCreatedOn()).appendText(",");
        appendField(description, "state", order.getState()).appendText(",");
        appendField(description, "shipmentId", order.getShipmentId()).appendText(",");
        appendDescriptionOf(description, "lineItems", matchingLineItems(order));
    }

    private UUID getCustomerId(Order order) {
        return order.getCustomer() == null ? null : order.getCustomer().getId();
    }
}
