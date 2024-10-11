package usfca.pyne.cs601.virtualfridge.Model;

import jakarta.persistence.Embeddable;

@Embeddable
public class RecipeIngredient {

    private String name;

    private double amount;

    private String unit;

    public RecipeIngredient(){}

    public RecipeIngredient(String name, double amount, String unit) {
        this.name = name.toLowerCase();
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                '}';
    }
}
