package usfca.pyne.cs601.virtualfridge.Controller;


import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usfca.pyne.cs601.virtualfridge.Model.FavoriteRecipe;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Service.FavoriteRecipeService;
import usfca.pyne.cs601.virtualfridge.Service.RecipeService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteRecipeController {

    private final FavoriteRecipeService favoriteRecipeService;

    public FavoriteRecipeController(FavoriteRecipeService favoriteRecipeService, RecipeService recipeService) {
        this.favoriteRecipeService = favoriteRecipeService;
    }

    @NotNull
    @GetMapping("/get")
    public ResponseEntity<List<Recipe>> getAllFavoriteRecipes() {
        List<FavoriteRecipe> favoriteRecipes = favoriteRecipeService.getAllFavoriteRecipes();
        List<Recipe> recipes = favoriteRecipes.stream()
                .map(FavoriteRecipe::getRecipe)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    @NotNull
    @PostMapping("/favorite/{recipeId}")
    public ResponseEntity<String> addFavoriteRecipe(@PathVariable Long recipeId) {
        boolean isFavorited = favoriteRecipeService.isRecipeFavorited(recipeId);
        if (isFavorited) {
            favoriteRecipeService.removeFavoriteRecipe(recipeId);
            return ResponseEntity.ok("Recipe removed from favorites.");
        } else {
            favoriteRecipeService.addFavoriteRecipe(recipeId);
            return ResponseEntity.ok("Recipe added to favorites.");
        }
    }

//    @DeleteMapping("/delete/{recipeId}")
//    public ResponseEntity<String> removeFavoriteRecipe(@PathVariable Long recipeId) {
//        favoriteRecipeService.removeFavoriteRecipe(recipeId);
//        return ResponseEntity.ok("Recipe removed from favorites.");
//    }
}
