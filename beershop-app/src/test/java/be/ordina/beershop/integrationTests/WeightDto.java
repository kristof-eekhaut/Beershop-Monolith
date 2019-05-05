package be.ordina.beershop.integrationTests;

public class WeightDto {
    private final String amount;
    private final String unit;


    public WeightDto(final String amount, final String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}
