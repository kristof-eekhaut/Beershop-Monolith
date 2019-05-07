package be.ordina.beershop.common;

import be.ordina.beershop.domain.SingleValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.UNNECESSARY;

public class Price extends SingleValueObject<BigDecimal> {

    public static final Price ZERO = Price.price(BigDecimal.ZERO);

    private Price(BigDecimal amount) {
        super(amount.setScale(2, RoundingMode.HALF_UP));

        if (getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Price can not be less than 0");
        }
    }

    public Price multiply(int quantity) {
        return price(getValue().multiply(new BigDecimal(quantity)));
    }

    public Price add(Price price) {
        return price(getValue().add(price.getValue()));
    }

    public Price applyDiscountPercentage(BigDecimal percentage) {
        BigDecimal discountPercentage = percentage.divide(BigDecimal.valueOf(100), UNNECESSARY);
        BigDecimal discountAmount = getValue().multiply(discountPercentage);
        return price(getValue().min(discountAmount));
    }

    public static Price price(BigDecimal value) {
        return new Price(value);
    }
}
