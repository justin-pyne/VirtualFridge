package usfca.pyne.cs601.virtualfridge.Service;

import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;
import usfca.pyne.cs601.virtualfridge.Model.FavoriteRecipe;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Repository.FavoriteRecipeRepository;
import usfca.pyne.cs601.virtualfridge.Repository.RecipeRepository;

import java.util.List;

@Service
public class FavoriteRecipeService {

    private final FavoriteRecipeRepository favoriteRecipeRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public FavoriteRecipeService(FavoriteRecipeRepository favoriteRecipeRepository, RecipeRepository recipeRepository, UserService userService) {
        this.favoriteRecipeRepository = favoriteRecipeRepository;
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    public List<FavoriteRecipe> getAllFavoriteRecipes(String email){
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        return favoriteRecipeRepository.findByUserId(userEntity.getId());
    }

    public FavoriteRecipe addFavoriteRecipe(String email, Long recipeId) {
        UserEntity userEntity = userService.findOrCreateUser(email, null, null, null);
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found with ID: " + recipeId));
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe(userEntity, recipe);
        return favoriteRecipeRepository.save(favoriteRecipe);
    }

    public void removeFavoriteRecipe(String email, Long recipeId) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        favoriteRecipeRepository.deleteByUserIdAndRecipeId(userEntity.getId(), recipeId);
    }

    public boolean isRecipeFavorited(String email, Long recipeId) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        return favoriteRecipeRepository.findByUserIdAndRecipeId(userEntity.getId(), recipeId).isPresent();
    }
}
