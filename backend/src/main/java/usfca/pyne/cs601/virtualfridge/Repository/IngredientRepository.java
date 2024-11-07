package usfca.pyne.cs601.virtualfridge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usfca.pyne.cs601.virtualfridge.Model.Ingredient;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
//    Optional<Ingredient> findByName(String name);
    Optional<Ingredient> findByFridgeIdAndName(Long fridgeId, String name);
    List<Ingredient> findByFridgeId(Long fridgeId);
    Optional<Ingredient> findByIdAndFridgeId(Long id, Long fridgeId);

//    default boolean deductIngredient(String name, double amount) {
//        Optional<Ingredient> ingredientOpt = findByName(name);
//        if (ingredientOpt.isPresent()) {
//            Ingredient ingredient = ingredientOpt.get();
//            if (ingredient.getAmount() >= amount) {
//                ingredient.setAmount(ingredient.getAmount() - amount);
//                if (ingredient.getAmount() <= 0) {
//                    delete(ingredient);
//                } else {
//                    save(ingredient);
//                }
//                return true;
//            }
//        }
//        return false;
//    }
}
