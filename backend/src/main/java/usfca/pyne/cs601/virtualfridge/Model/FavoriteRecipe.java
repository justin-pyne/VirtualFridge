package usfca.pyne.cs601.virtualfridge.Model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;


@Entity
@Table(name = "favorite_recipes")
public class FavoriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "recipe_id", unique = true, nullable = false)
    @NotNull(message = "Recipe is required.")
    private Recipe recipe;

    public FavoriteRecipe(){}

    public FavoriteRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Long getId() {
        return id;
    }


    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "FavoriteRecipe{" +
                "id=" + id +
                ", recipe=" + recipe +
                '}';
    }
}
