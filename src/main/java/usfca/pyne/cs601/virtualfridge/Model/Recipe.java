package usfca.pyne.cs601.virtualfridge.Model;

import lombok.Getter;

import java.util.List;

public class Recipe {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String preparationTime;


    private List<Ingredient> ingredients;
    private List<String> instructions;


    public Recipe(){}

    public Recipe(String name, String description, String preparationTime, List<Ingredient> ingredients, List<String> instructions) {
        this.name = name;
        this.description = description;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public List<Ingredient> getIngredients() {
        return List.copyOf(ingredients);
    }

    public List<String> getInstructions() {
        return List.copyOf(instructions);
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preparationTime='" + preparationTime + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                '}';
    }
}
