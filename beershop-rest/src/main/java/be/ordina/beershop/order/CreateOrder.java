package be.ordina.beershop.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrder {

    @NotNull
    private final String customerId;
    private final Address shipmentAddress;

    @JsonCreator
    public CreateOrder(@JsonProperty("customerId") final String customerId,
                       @JsonProperty("shipmentAddress") final Address shipmentAddress) {
        this.customerId = customerId;
        this.shipmentAddress = shipmentAddress;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Address getShipmentAddress() {
        return shipmentAddress;
    }

    public static class Address {

        @NotNull
        private String street;
        @NotNull
        private String number;
        @NotNull
        private String postalCode;
        @NotNull
        private String country;

        private Address(@JsonProperty("street") final String street,
                        @JsonProperty("number") final String number,
                        @JsonProperty("postalCode") final String postalCode,
                        @JsonProperty("country") final String country) {
            this.street = street;
            this.number = number;
            this.postalCode = postalCode;
            this.country = country;
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

            public Address build() {
                return new Address(
                        street,
                        number,
                        postalCode,
                        country
                );
            }
        }
    }
}
