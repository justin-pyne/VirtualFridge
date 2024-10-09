package usfca.pyne.cs601.virtualfridge;

import java.util.List;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Service.ChatModelTestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import usfca.pyne.cs601.virtualfridge.Service.RecipeService;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

//    private final ChatModelTestService openAITestService;
    private final RecipeService recipeService;

    public CommandLineAppStartupRunner(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void run(String... args) throws Exception {
//        String prompt = "Hello, how are you?";
//        String response = openAITestService.getOpenAIResponse(prompt);
//        System.out.println("AI Response: " + response);

        String ingredients = "1 lb Ground beef, 2 onions, 2 tomatoes, 1 can of tomato paste, chicken stock, carrots, sweet potato, corn, peas, cilantro, lime, cheese";
        String response = recipeService.generateRecipe(ingredients);
        System.out.println("Raw response: ");
        System.out.println(response);
        List<Recipe> recipes = recipeService.parseResponseToRecipes(response);
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                System.out.println(recipe);
            }
        } else {
            System.err.println("No recipes were generated.");
        }
    }
}
