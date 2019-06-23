package be.ordina.beershop.order;

import be.ordina.beershop.domain.AbstractValueObject;

public class ShipmentAddress extends AbstractValueObject {

    private String street;
    private String number;
    private String postalCode;
    private String country;

    private ShipmentAddress(Builder builder) {
        street = builder.street;
        number = builder.number;
        postalCode = builder.postalCode;
        country = builder.country;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String street;
        private String number;
        private String postalCode;
        private String country;

        private Builder() {
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public ShipmentAddress build() {
            return new ShipmentAddress(this);
        }
    }
}
