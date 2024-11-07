package usfca.pyne.cs601.virtualfridge.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Setter
@Entity
@Table(name = "recipes")
public class Recipe {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    @NotNull
    private String name;

    @Getter
    private String description;

    @Getter
    private String preparationTime;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_instructions", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<String> instructions;


    public Recipe(){}

    @JsonCreator
    public Recipe(
            @NotNull @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("preparationTime") String preparationTime,
            @JsonProperty("ingredients") List<RecipeIngredient> ingredients,
            @JsonProperty("instructions") List<String> instructions) {
        this.name = name;
        this.description = description;
        this.preparationTime = preparationTime;
        if (ingredients != null) {
            for (RecipeIngredient ingredient : ingredients) {
                ingredient.setRecipe(this);
            }
        }
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public List<RecipeIngredient> getIngredients() {
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
