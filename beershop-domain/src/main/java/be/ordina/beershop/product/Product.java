package be.ordina.beershop.product;

import be.ordina.beershop.domain.AbstractAggregateRoot;
import be.ordina.beershop.product.event.ProductCreatedEvent;
import be.ordina.beershop.product.event.ProductStockChangedEvent;
import be.ordina.beershop.product.exception.InvalidProductQuantityException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Product extends AbstractAggregateRoot<ProductId> {

    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal alcoholPercentage;
    private Weight weight;

    private final List<Discount> discounts = new ArrayList<>();

    private Product(Builder builder) {
        super(builder.id);

        setName(builder.name);
        setQuantity(builder.quantity);
        setPrice(builder.price);
        discounts.addAll(builder.discounts);
        setAlcoholPercentage(builder.alcoholPercentage);
        setWeight(builder.weight);

        registerEvent(ProductCreatedEvent.builder()
                .productId(getId())
                .name(name)
                .alcoholPercentage(alcoholPercentage)
                .weight(weight)
                .quantity(quantity)
                .price(price)
                .build());
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Optional<Weight> getWeight() {
        return Optional.ofNullable(weight);
    }

    public List<Discount> getDiscounts() {
        return new ArrayList<>(discounts);
    }

    public Optional<BigDecimal> getAlcoholPercentage() {
        return Optional.ofNullable(alcoholPercentage);
    }

    public QuantityIndicator getQuantityIndicator() {
        if(quantity == 0) {
            return QuantityIndicator.SOLD_OUT;
        } else if(quantity < 5) {
            return QuantityIndicator.ALMOST_SOLD_OUT;
        } else {
            return QuantityIndicator.PLENTY_AVAILABLE;
        }
    }

    public void changeStockQuantity(int quantity) {
        setQuantity(quantity);

        registerEvent(new ProductStockChangedEvent(getId(), quantity));
    }

    @Deprecated // TODO: create separate Discount Aggregate
    public void addDiscount(BigDecimal percentage, LocalDate startDate, LocalDate endDate) {
        Discount discount = Discount.builder()
                .percentage(percentage)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        discounts.add(discount);
    }

    private void setName(String name) {
        this.name = requireNonNull(name);
    }

    private void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidProductQuantityException("Quantity can not be less than 0.");
        }
        this.quantity = quantity;
    }

    private void setPrice(BigDecimal price) {
        this.price = requireNonNull(price);
    }

    private void setWeight(final Weight weight) {
        this.weight = weight;
    }

    private void setAlcoholPercentage(final BigDecimal alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ProductId id;
        private String name;
        private int quantity;
        private BigDecimal price;
        private List<Discount> discounts = new ArrayList<>();
        private BigDecimal alcoholPercentage;
        private Weight weight;

        private Builder() {
        }

        public Builder id(ProductId id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder discounts(List<Discount> discounts) {
            this.discounts = discounts;
            return this;
        }

        public Builder alcoholPercentage(BigDecimal alcoholPercentage) {
            this.alcoholPercentage = alcoholPercentage;
            return this;
        }

        public Builder weight(Weight weight) {
            this.weight = weight;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
