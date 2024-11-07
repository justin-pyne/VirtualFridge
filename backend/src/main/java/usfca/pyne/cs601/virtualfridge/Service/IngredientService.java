package usfca.pyne.cs601.virtualfridge.Service;

import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;
import usfca.pyne.cs601.virtualfridge.Model.Fridge;
import usfca.pyne.cs601.virtualfridge.Model.Ingredient;
import usfca.pyne.cs601.virtualfridge.Repository.IngredientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserService userService;

    public IngredientService(IngredientRepository ingredientRepository, UserService userService) {
        this.ingredientRepository = ingredientRepository;
        this.userService = userService;
    }

    public List<Ingredient> getAllIngredients(String email) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        Fridge fridge = userEntity.getFridge();
        return ingredientRepository.findByFridgeId(fridge.getId());
    }

    public Ingredient addOrUpdateIngredient(String email, Ingredient ingredient){
        UserEntity userEntity = userService.findOrCreateUser(email, null, null, null);
        Fridge fridge = userEntity.getFridge();

        Optional<Ingredient> existingIngredientOpt = ingredientRepository.findByFridgeIdAndName(fridge.getId(), ingredient.getName().toLowerCase());
        if (existingIngredientOpt.isPresent()) {
            Ingredient existingIngredient = existingIngredientOpt.get();
            existingIngredient.setAmount(existingIngredient.getAmount() + ingredient.getAmount());
            existingIngredient.setUnit(ingredient.getUnit());
            existingIngredient.setExpirationDate(ingredient.getExpirationDate());
            return ingredientRepository.save(existingIngredient);
        } else {
            ingredient.setFridge(fridge);
            ingredient.setName(ingredient.getName().toLowerCase());
            return ingredientRepository.save(ingredient);
        }
    }

    public boolean deductIngredient (String email, String name, double amount) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        Fridge fridge = userEntity.getFridge();
        Optional<Ingredient> ingredientOpt = ingredientRepository.findByFridgeIdAndName(fridge.getId(), name.toLowerCase());
        if (ingredientOpt.isPresent()) {
            Ingredient ingredient = ingredientOpt.get();
            if (ingredient.getAmount() >= amount) {
                ingredient.setAmount(ingredient.getAmount() - amount);
                if (ingredient.getAmount() <= 0) {
                    ingredientRepository.delete(ingredient);
                } else {
                    ingredientRepository.save(ingredient);
                }
                return true;
            }
        }
        return false;
    }

    public void removeIngredient(String email, Long id) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);

        Fridge fridge = userEntity.getFridge();
        Optional<Ingredient> ingredientOpt = ingredientRepository.findByIdAndFridgeId(id, fridge.getId());
        if (ingredientOpt.isPresent()) {
            ingredientRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Ingredient not found or does not belong to the user's fridge.");
        }
    }
}
