package be.ordina.beershop.integrationTests;

public class ProductDto {

    private final  String name;
    private final int quantity;
    private final String price;
    private final String alcoholPercentage;
    private final WeightDto weight;


    public ProductDto(final String name, final int quantity, final String price, final String alcoholPercentage, final WeightDto weight) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.alcoholPercentage = alcoholPercentage;
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public String getPrice() {
        return price;
    }

    public WeightDto getWeight() {
        return weight;
    }
}
