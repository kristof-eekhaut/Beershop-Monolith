package be.ordina.beershop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "PRODUCT")
public class Product {

    @Id
    @Column(name = "ID")
    private UUID id;
    @Column(name = "NAME")
    @NotNull
    private String name;
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "PRICE")
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal price;
    @Column(name = "CREATED_ON")
    @JsonIgnore
    private LocalDateTime createdOn;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts = new ArrayList<>();
    @Column(name = "ALCOHOL_PERCENTAGE")
    @NotNull
    private BigDecimal alcoholPercentage;

    @Embedded
    @Valid
    private Weight weight;

    public Product() {
    }

    public Product(UUID id, String name, int quantity, BigDecimal price, BigDecimal alcoholPercentage) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.alcoholPercentage = alcoholPercentage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public int getQuantity() {
        return quantity;
    }

    @JsonProperty
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(final Weight weight) {
        this.weight = weight;
    }

    public void addDiscount(final Discount discount) {
        discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return new ArrayList<>(discounts);
    }

    public BigDecimal getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(final BigDecimal alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    @JsonProperty
    public QuantityIndicator getQuantityIndicator() {
        if(quantity == 0) {
            return QuantityIndicator.SOLD_OUT;
        } else if(quantity < 5) {
            return QuantityIndicator.ALMOST_SOLD_OUT;
        } else {
            return QuantityIndicator.PLENTY_AVAILABLE;
        }
    }
}
