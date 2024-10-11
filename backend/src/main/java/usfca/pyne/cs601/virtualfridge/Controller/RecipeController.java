package usfca.pyne.cs601.virtualfridge.Controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Service.RecipeService;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @NotNull
    @GetMapping("/get")
    public ResponseEntity<List<Recipe>> getGeneratedRecipes(){
        ResponseEntity.status(HttpStatus.OK).body("Generating new recipes");
        List<Recipe> recipes = recipeService.generateRecipe();
        return ResponseEntity.ok(recipes);
    }

    @NotNull
    @PostMapping("/cook/{recipeId}")
    public ResponseEntity<String> cookRecipe(@PathVariable Long recipeId) {
        boolean success = recipeService.cookRecipe(recipeId);
        if(success){
            return ResponseEntity.ok("Recipe cooked successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient ingredients for this recipe.");
        }
    }
}
