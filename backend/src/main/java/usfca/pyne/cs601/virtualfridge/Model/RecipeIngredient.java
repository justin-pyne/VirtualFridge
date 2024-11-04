package usfca.pyne.cs601.virtualfridge.Model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
@Embeddable
public class RecipeIngredient {

    @NotNull
    private String name;

    private double amount;

    @NotNull
    private String unit;

    public RecipeIngredient(){}

    public RecipeIngredient(String name, double amount, String unit) {
        this.name = name.toLowerCase();
        this.amount = amount;
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
