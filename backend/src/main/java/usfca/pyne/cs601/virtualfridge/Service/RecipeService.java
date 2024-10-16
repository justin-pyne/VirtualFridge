package usfca.pyne.cs601.virtualfridge.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;
import usfca.pyne.cs601.virtualfridge.Model.Ingredient;
import usfca.pyne.cs601.virtualfridge.Model.RecipeIngredient;
import usfca.pyne.cs601.virtualfridge.Repository.FavoriteRecipeRepository;
import usfca.pyne.cs601.virtualfridge.Repository.IngredientRepository;
import usfca.pyne.cs601.virtualfridge.Repository.RecipeRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {


    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final FavoriteRecipeRepository favoriteRecipeRepository;
    private final ChatLanguageModel chatLanguageModel;


    public RecipeService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, FavoriteRecipeRepository favoriteRecipeRepository, ChatLanguageModel chatLanguageModel) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.favoriteRecipeRepository = favoriteRecipeRepository;
        this.chatLanguageModel = chatLanguageModel;
    }

    public List<Recipe> generateRecipe(){
        List<Ingredient> fridgeIngredients = ingredientRepository.findAll();

        if (fridgeIngredients.isEmpty()) {
            throw new IllegalStateException("No ingredients available in the fridge.");
        }

//        fridgeIngredients.sort(Comparator.comparing(Ingredient::getExpirationDate));

        List<String> ingredientNames = new ArrayList<>();
        for (Ingredient ingredient : fridgeIngredients){
            ingredientNames.add(ingredient.getName());
        }

        CreateRecipePrompt createRecipePrompt = new CreateRecipePrompt();
        createRecipePrompt.ingredients = ingredientNames;
        createRecipePrompt.numberOfRecipes = 5;
        Prompt prompt = StructuredPromptProcessor.toPrompt(createRecipePrompt);

        try{
            System.out.println("Querying the api...");
            AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
            String aiString = aiMessage.text();
            List<Recipe> recipes = parseResponseToRecipes(extractJson(aiString));

            recipeRepository.saveAll(recipes);
            return recipes;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private String extractJson(String response){
        int startIndex = response.indexOf('[');
        int endIndex = response.lastIndexOf(']');
        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex){
            throw new IllegalStateException("No valid JSON array found in the response.");
        }
        return response.substring(startIndex, endIndex + 1).trim();
    }

    private List<Recipe> parseResponseToRecipes(String json) {
        Gson gson = new Gson();
        Type recipeListType = new TypeToken<List<Recipe>>() {}.getType();
        try {
            List<Recipe> recipes = gson.fromJson(json, recipeListType);
            return recipes;
        } catch (JsonSyntaxException e){
            System.out.println("Failed to parse JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }



    @StructuredPrompt({
            "Create {{numberOfRecipes}} recipes that can be prepared using the following ingredients, prioritizing ingredients that are closest to their expiration date: {{ingredients}}.",
            "",
            "You do not need to use all of the ingredients. Do not add ingredients that are not listed. Just generate the best possible recipes.",
            "Return the recipes in JSON format as an array, with the following fields for each recipe:",
            "- name (string)",
            "- description (string)",
            "- preparationTime (string, e.g., '30 minutes')",
            "- ingredients (array of objects), where each ingredient object has:",
            "  - name (string)",
            "  - amount (number, use decimal for fractions)",
            "  - unit (string, e.g., 'grams', 'pieces', 'cups')",
            "- instructions (array of strings)",
            "",
            "Example JSON output:",
            "[",
            "  {",
            "    \"name\": \"Recipe Name\",",
            "    \"description\": \"Description of the recipe.\",",
            "    \"preparationTime\": \"Preparation time in minutes.\",",
            "    \"ingredients\": [",
            "      {",
            "        \"name\": \"ingredient1\",",
            "        \"amount\": 200.0,",
            "        \"unit\": \"grams\"",
            "      },",
            "      {",
            "        \"name\": \"ingredient2\",",
            "        \"amount\": 2.0,",
            "        \"unit\": \"pieces\"",
            "      }",
            "    ],",
            "    \"instructions\": [\"Step 1\", \"Step 2\"]",
            "  }",
            "]",
            "",
            "For ingredients, include amounts after a comma as shown in the example output.",
            "Please use one of the following units depending on what makes sense for the ingredient:",
            "- For solid ingredients: 'grams' or 'pounds'",
            "- For liquids: 'milliliters' or 'cups'",
            "- For countable items: 'pieces'",
            "**Important Instructions:**",
            "Ensure that amounts are provided in these units.",
            "Do not include markdown, explanations, or comments.",
            "Please return only valid JSON and no additional text."
    })
    static class CreateRecipePrompt{
        private List<String> ingredients;
        private int numberOfRecipes;
    }

    public Recipe getRecipeById (Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found with ID: " + recipeId));
    }

    @Transactional
    public boolean cookRecipe (Long recipeId) {
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isPresent()) {
            Recipe recipes = recipeOpt.get();
            for (RecipeIngredient recipeIngredient : recipes.getIngredients()) {
                String ingredientName = recipeIngredient.getName();
                double requiredAmount = recipeIngredient.getAmount();

                Optional<Ingredient> optionalFridgeIngredient = ingredientRepository.findByName(ingredientName);
                if (optionalFridgeIngredient.isPresent()) {
                    boolean deducted = ingredientRepository.deductIngredient(ingredientName.toLowerCase(), requiredAmount);
                    if (!deducted) {
                        return false;
                    }
                } else {
                        return false;
                    }
                }
            return true;
            }
        return false;
        }

    public boolean isRecipeFavorited(Long recipeId) {
        return favoriteRecipeRepository.findByRecipeId(recipeId).isPresent();
    }
}
