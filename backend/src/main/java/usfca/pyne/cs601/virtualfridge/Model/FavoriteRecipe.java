package usfca.pyne.cs601.virtualfridge.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;


@Getter
@Entity
@Table(name = "favorite_recipes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "recipe_id"}))
public class FavoriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Setter
    @OneToOne
    @JoinColumn(name = "recipe_id", unique = true, nullable = false)
    private Recipe recipe;

    public FavoriteRecipe(){}

    public FavoriteRecipe(UserEntity user, Recipe recipe) {
        this.user = user;
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
