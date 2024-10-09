package usfca.pyne.cs601.virtualfridge.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Recipe {
    private String name;
    private String description;
    private String preparationTime;


    private List<Ingredient> ingredients;
    private List<String> instructions;


    public Recipe(){}

    @JsonCreator
    public Recipe(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("preparationTime") String preparationTime,
            @JsonProperty("ingredients") List<Ingredient> ingredients,
            @JsonProperty("instructions") List<String> instructions) {
        this.name = name;
        this.description = description;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPreparationTime() {
        return preparationTime;
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
