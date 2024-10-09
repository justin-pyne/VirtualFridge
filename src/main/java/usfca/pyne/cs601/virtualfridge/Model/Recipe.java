package usfca.pyne.cs601.virtualfridge.Model;

import java.util.List;

public class Recipe {
    private String name;
    private String description;
    private String preparationTime;
    private List<String> ingredients;
    private List<String> instructions;


    public Recipe(String name, String description, String preparationTime, List<String> ingredients, List<String> instructions) {
        this.name = name;
        this.description = description;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return List.copyOf(ingredients);
    }

    public List<String> getInstructions() {
        return List.copyOf(instructions);
    }

    public String getDescription() {
        return description;
    }

    public String getPreparationTime() {
        return preparationTime;
    }
}
