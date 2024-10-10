package usfca.pyne.cs601.virtualfridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usfca.pyne.cs601.virtualfridge.Model.FavoriteRecipe;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<FavoriteRecipe, Long> {
    Optional<FavoriteRecipe> findByRecipe(Recipe recipe);
    Optional<FavoriteRecipe> findByRecipeId(Long recipeId);
}
