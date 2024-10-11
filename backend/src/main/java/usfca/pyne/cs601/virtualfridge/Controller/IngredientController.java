package usfca.pyne.cs601.virtualfridge.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usfca.pyne.cs601.virtualfridge.Model.Ingredient;
import usfca.pyne.cs601.virtualfridge.Service.IngredientService;

import java.util.List;

@RestController
@RequestMapping("/fridge")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients(){
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Ingredient> addOrUpdateIngredient(@RequestBody Ingredient ingredient){
        Ingredient addedIngredient = ingredientService.addOrUpdateIngredient(ingredient);
        return ResponseEntity.ok(addedIngredient);
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.removeIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
