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

    public FavoriteRecipeController(FavoriteRecipeService favoriteRecipeService) {
        this.favoriteRecipeService = favoriteRecipeService;
    }

    @NotNull
    @GetMapping("/get")
    public ResponseEntity<List<Recipe>> getAllFavoriteRecipes(@RequestParam("email") String email) {
        List<FavoriteRecipe> favoriteRecipes = favoriteRecipeService.getAllFavoriteRecipes(email);
        List<Recipe> recipes = favoriteRecipes.stream()
                .map(FavoriteRecipe::getRecipe)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    @NotNull
    @PostMapping("/favorite/{recipeId}")
    public ResponseEntity<String> addFavoriteRecipe(@RequestParam("email") String email,
                                                    @RequestParam("recipeId") Long recipeId) {
        boolean isFavorited = favoriteRecipeService.isRecipeFavorited(email, recipeId);
        if (isFavorited) {
            favoriteRecipeService.removeFavoriteRecipe(email, recipeId);
            return ResponseEntity.ok("Recipe removed from favorites.");
        } else {
            favoriteRecipeService.addFavoriteRecipe(email, recipeId);
            return ResponseEntity.ok("Recipe added to favorites.");
        }
    }

//    @DeleteMapping("/delete/{recipeId}")
//    public ResponseEntity<String> removeFavoriteRecipe(@PathVariable Long recipeId) {
//        favoriteRecipeService.removeFavoriteRecipe(recipeId);
//        return ResponseEntity.ok("Recipe removed from favorites.");
//    }
}
