package be.ordina.beershop.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
public class Address {

    @Column(name = "STREET")
    @NotBlank
    private String street;
    @Column(name = "NUMBER")
    @NotBlank
    private String number;
    @Column(name = "POSTAL_CODE")
    @NotBlank
    private String postalCode;
    @Column(name = "COUNTRY")
    @NotBlank
    private String country;

    public Address() {
        // For Hibernate
    }

    private Address(Builder builder) {
        setStreet(builder.street);
        setNumber(builder.number);
        setPostalCode(builder.postalCode);
        setCountry(builder.country);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(number, address.number) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, postalCode, country);
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @NotBlank String street;
        private @NotBlank String number;
        private @NotBlank String postalCode;
        private @NotBlank String country;

        private Builder() {
        }

        public Builder street(@NotBlank String street) {
            this.street = street;
            return this;
        }

        public Builder number(@NotBlank String number) {
            this.number = number;
            return this;
        }

        public Builder postalCode(@NotBlank String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder country(@NotBlank String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
