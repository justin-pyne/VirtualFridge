package usfca.pyne.cs601.virtualfridge.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@Getter
@Entity
@Table(name = "favorite_recipes")
public class FavoriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne
    @JoinColumn(name = "recipe_id", unique = true, nullable = false)
    private Recipe recipe;

    public FavoriteRecipe(){}

    public FavoriteRecipe(Recipe recipe) {
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
