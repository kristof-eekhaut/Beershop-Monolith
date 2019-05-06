package be.ordina.beershop.repository.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "CUSTOMER")
public class Customer {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SHOPPING_CART_ID")
    private ShoppingCart shoppingCart = new ShoppingCart();

    @Embedded
    private Address address;

    public Customer() {
        // For Hibernate
    }

    private Customer(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setBirthDate(builder.birthDate);
        setShoppingCart(builder.shoppingCart);
        setAddress(builder.address);
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(final ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String name;
        private LocalDate birthDate;
        private ShoppingCart shoppingCart = new ShoppingCart();
        private Address address;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder shoppingCart(ShoppingCart shoppingCart) {
            this.shoppingCart = shoppingCart;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
