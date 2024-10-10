package usfca.pyne.cs601.virtualfridge.Service;

import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Model.FavoriteRecipe;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Repository.FavoriteRecipeRepository;
import usfca.pyne.cs601.virtualfridge.Repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteRecipeService {

    private final FavoriteRecipeRepository favoriteRecipeRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteRecipeService(FavoriteRecipeRepository favoriteRecipeRepository, RecipeRepository recipeRepository) {
        this.favoriteRecipeRepository = favoriteRecipeRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<FavoriteRecipe> getAllFavoriteRecipes(){
        return favoriteRecipeRepository.findAll();
    }

    public FavoriteRecipe addFavoriteRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found with ID: " + recipeId));
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe(recipe);
        return favoriteRecipeRepository.save(favoriteRecipe);
    }

    public void removeFavoriteRecipe(Long recipeId) {
        Optional<FavoriteRecipe> favoriteRecipeOpt = favoriteRecipeRepository.findByRecipeId(recipeId);
        favoriteRecipeOpt.ifPresent(favoriteRecipeRepository::delete);
    }

    public boolean isRecipeFavorited(Long recipeId) {
        return favoriteRecipeRepository.findByRecipeId(recipeId).isPresent();
    }
}
