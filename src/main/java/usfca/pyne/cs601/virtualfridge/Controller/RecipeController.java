package usfca.pyne.cs601.virtualfridge.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Service.RecipeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fridge")
public class RecipeController {

    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    private RecipeService recipeService;

    @PostMapping("/ingredients")
    public ResponseEntity<List<Recipe>> getRecipes(@RequestBody String ingredients){
        List<Recipe> recipes = recipeService.generateRecipe(ingredients);
        return ResponseEntity.ok().body(recipes);
    }

}
