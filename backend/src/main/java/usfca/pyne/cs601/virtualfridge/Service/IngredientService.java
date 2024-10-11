package usfca.pyne.cs601.virtualfridge.Service;

import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Model.Ingredient;
import usfca.pyne.cs601.virtualfridge.Repository.IngredientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient addOrUpdateIngredient(Ingredient ingredient){
        Optional<Ingredient> existingIngredientOpt = ingredientRepository.findByName(ingredient.getName().toLowerCase());
        if (existingIngredientOpt.isPresent()) {
            Ingredient existingIngredient = existingIngredientOpt.get();
            existingIngredient.setAmount(existingIngredient.getAmount() + ingredient.getAmount());
            existingIngredient.setUnit(ingredient.getUnit());
//            existingIngredient.setExpirationDate(ingredient.getExpirationDate());
            return ingredientRepository.save(existingIngredient);
        } else {
            return ingredientRepository.save(ingredient);
        }
    }

    public boolean deductIngredient (String name, double amount) {
        Optional<Ingredient> ingredientOpt = ingredientRepository.findByName(name.toLowerCase());
        if (ingredientOpt.isPresent()) {
            Ingredient ingredient = ingredientOpt.get();
            if (ingredient.getAmount() >= amount) {
                ingredient.setAmount(ingredient.getAmount() - amount);
                ingredientRepository.save(ingredient);
                return true;
            }
        }
        return false;
    }

    public void removeIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}
