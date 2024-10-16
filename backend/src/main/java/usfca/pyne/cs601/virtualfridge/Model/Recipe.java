package usfca.pyne.cs601.virtualfridge.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    private String description;

    private String preparationTime;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns  = @JoinColumn(name = "recipe_id"))
    private List<RecipeIngredient> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_instructions", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> instructions;


    public Recipe(){}

    @JsonCreator
    public Recipe(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("preparationTime") String preparationTime,
            @JsonProperty("ingredients") List<RecipeIngredient> ingredients,
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

    public List<RecipeIngredient> getIngredients() {
        return List.copyOf(ingredients);
    }

    public List<String> getInstructions() {
        return List.copyOf(instructions);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
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
