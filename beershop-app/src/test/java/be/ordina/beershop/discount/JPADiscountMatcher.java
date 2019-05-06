package be.ordina.beershop.discount;

import be.ordina.beershop.domain.matcher.BaseEntityMatcher;
import be.ordina.beershop.repository.entities.JPADiscount;
import org.hamcrest.Description;

public class JPADiscountMatcher extends BaseEntityMatcher<JPADiscount> {

    public static JPADiscountMatcher matchesDiscount(JPADiscount discount) {
        return new JPADiscountMatcher(discount);
    }

    private JPADiscountMatcher(JPADiscount discount) {
        super(discount);
    }

    @Override
    protected final boolean matchesSafely(JPADiscount other) {
        return isEqual(this.objectToMatch.getPercentage(), other.getPercentage())
                && isEqual(this.objectToMatch.getStartDate(), other.getStartDate())
                && isEqual(this.objectToMatch.getEndDate(), other.getEndDate());
    }

    @Override
    protected void describeToEntity(Description description, JPADiscount discount) {
        appendField(description, "percentage", discount.getPercentage()).appendText(",");
        appendField(description, "startDate", discount.getStartDate()).appendText(",");
        appendField(description, "endDate", discount.getEndDate()).appendText(",");
    }
}
