package usfca.pyne.cs601.virtualfridge.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usfca.pyne.cs601.virtualfridge.Model.FavoriteRecipe;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Service.FavoriteRecipeService;
import usfca.pyne.cs601.virtualfridge.Service.RecipeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
public class FavoriteRecipeController {

    private final FavoriteRecipeService favoriteRecipeService;
    private final RecipeService recipeService;

    public FavoriteRecipeController(FavoriteRecipeService favoriteRecipeService, RecipeService recipeService) {
        this.favoriteRecipeService = favoriteRecipeService;
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllFavoriteRecipes() {
        List<FavoriteRecipe> favoriteRecipes = favoriteRecipeService.getAllFavoriteRecipes();
        List<Recipe> recipes = favoriteRecipes.stream()
                .map(FavoriteRecipe::getRecipe)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    @PostMapping
    public ResponseEntity<String> addFavoriteRecipe(@RequestParam Long recipeId) {
        boolean isFavorited = favoriteRecipeService.isRecipeFavorited(recipeId);
        if(isFavorited) {
            return ResponseEntity.badRequest().body("Recipe is already favorited.");
        }
        favoriteRecipeService.addFavoriteRecipe(recipeId);
        return ResponseEntity.ok("Recipe added to favorites.");
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> removeFavoriteRecipe(@PathVariable Long recipeId) {
        favoriteRecipeService.removeFavoriteRecipe(recipeId);
        return ResponseEntity.ok("Recipe removed from favorites.");
    }
}
