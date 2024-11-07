package usfca.pyne.cs601.virtualfridge.Controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usfca.pyne.cs601.virtualfridge.Model.Ingredient;
import usfca.pyne.cs601.virtualfridge.Service.IngredientService;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @NotNull
    @GetMapping("/get")
    public ResponseEntity<List<Ingredient>> getAllIngredients(@RequestParam("email") String email){
        List<Ingredient> ingredients = ingredientService.getAllIngredients(email);
        return ResponseEntity.ok(ingredients);
    }

    @NotNull
    @PostMapping("/add")
    public ResponseEntity<Ingredient> addOrUpdateIngredient(@RequestParam("email") String email,
                                                            @RequestBody Ingredient ingredient) {
//        if (ingredient != null) {
            Ingredient addedIngredient = ingredientService.addOrUpdateIngredient(email, ingredient);
            return ResponseEntity.ok(addedIngredient);
//        }
//        return ResponseEntity.noContent().build();
    }

    @NotNull
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIngredient(@RequestParam("email") String email,
                                                 @PathVariable Long id) {
        ingredientService.removeIngredient(email, id);
        return ResponseEntity.noContent().build();
    }
}
