package be.ordina.beershop.product;

import be.ordina.beershop.domain.matcher.BaseEntityMatcher;
import be.ordina.beershop.discount.JPADiscountMatcher;
import be.ordina.beershop.repository.entities.JPADiscount;
import be.ordina.beershop.repository.entities.JPAProduct;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class JPAProductMatcher extends BaseEntityMatcher<JPAProduct> {

    private final Matcher<? super List<JPADiscount>> discounts;

    public static JPAProductMatcher matchesProduct(JPAProduct product) {
        return new JPAProductMatcher(product);
    }

    private JPAProductMatcher(JPAProduct product) {
        super(product);
        this.discounts = matchingDiscounts(product);
    }

    private Matcher<? super List<JPADiscount>> matchingDiscounts(JPAProduct product) {
        List<Matcher<? super JPADiscount>> discountsMatchers = product.getDiscounts().stream()
                .map(JPADiscountMatcher::matchesDiscount)
                .collect(Collectors.toList());
        return containsInAnyOrder(discountsMatchers);
    }

    @Override
    protected final boolean matchesSafely(JPAProduct other) {
        return isEqual(this.objectToMatch.getName(), other.getName())
                && isEqual(this.objectToMatch.getQuantity(), other.getQuantity())
                && isEqual(this.objectToMatch.getPrice(), other.getPrice())
                && isEqual(this.objectToMatch.getAlcoholPercentage(), other.getAlcoholPercentage())
                && isEqual(this.objectToMatch.getWeight(), other.getWeight())
                && this.discounts.matches(other.getDiscounts());
    }

    @Override
    protected void describeToEntity(Description description, JPAProduct product) {
        appendField(description, "name", product.getName()).appendText(",");
        appendField(description, "quantity", product.getQuantity()).appendText(",");
        appendField(description, "price", product.getPrice()).appendText(",");
        appendField(description, "alcoholPercentage", product.getAlcoholPercentage()).appendText(",");
        appendField(description, "weight", product.getWeight()).appendText(",");
        appendDescriptionOf(description, "discounts", matchingDiscounts(product));
    }
}
