package be.ordina.beershop.product;

import be.ordina.beershop.common.matcher.BaseEntityMatcher;
import be.ordina.beershop.discount.DiscountMatcher;
import be.ordina.beershop.domain.Discount;
import be.ordina.beershop.domain.Product;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class ProductMatcher extends BaseEntityMatcher<Product> {

    private final Matcher<? super List<Discount>> discounts;

    public static ProductMatcher matchesProduct(Product product) {
        return new ProductMatcher(product);
    }

    private ProductMatcher(Product product) {
        super(product);
        this.discounts = matchingDiscounts(product);
    }

    private Matcher<? super List<Discount>> matchingDiscounts(Product product) {
        List<Matcher<? super Discount>> discountsMatchers = product.getDiscounts().stream()
                .map(DiscountMatcher::matchesDiscount)
                .collect(Collectors.toList());
        return containsInAnyOrder(discountsMatchers);
    }

    @Override
    protected final boolean matchesSafely(Product other) {
        return isEqual(this.objectToMatch.getName(), other.getName())
                && isEqual(this.objectToMatch.getQuantity(), other.getQuantity())
                && isEqual(this.objectToMatch.getPrice(), other.getPrice())
//                && isEqual(this.objectToMatch.getCreatedOn(), other.getCreatedOn())  TODO:  inject Clock to be able to test with FixedClock
                && isEqual(this.objectToMatch.getAlcoholPercentage(), other.getAlcoholPercentage())
                && isEqual(this.objectToMatch.getWeight(), other.getWeight())
                && this.discounts.matches(other.getDiscounts());
    }

    @Override
    protected void describeToEntity(Description description, Product product) {
        appendField(description, "name", product.getName()).appendText(",");
        appendField(description, "quantity", product.getQuantity()).appendText(",");
        appendField(description, "price", product.getPrice()).appendText(",");
//        appendField(description, "createdOn", product.getCreatedOn()).appendText(",");
        appendField(description, "alcoholPercentage", product.getAlcoholPercentage()).appendText(",");
        appendField(description, "weight", product.getWeight()).appendText(",");
        appendDescriptionOf(description, "discounts", matchingDiscounts(product));
    }
}
