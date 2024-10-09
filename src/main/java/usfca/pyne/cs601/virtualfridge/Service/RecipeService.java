package usfca.pyne.cs601.virtualfridge.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Model.Recipe;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class RecipeService {

    private final ChatLanguageModel chatLanguageModel;

    public RecipeService (ChatLanguageModel chatLanguageModel){
        this.chatLanguageModel = chatLanguageModel;
    }

    public List<Recipe> generateRecipe(String ingredients){
        CreateRecipePrompt createRecipePrompt = new CreateRecipePrompt();
        createRecipePrompt.ingredients = ingredients;
        createRecipePrompt.numberOfRecipes = 5;
        Prompt prompt = StructuredPromptProcessor.toPrompt(createRecipePrompt);

        try{
            System.out.println("Querying the api...");
            AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
            String aiString = aiMessage.text();
            return parseResponseToRecipes(extractJson(aiString));
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
            "Create {{numberOfRecipes}} recipes that can be prepared using the following ingredients: {{ingredients}}.",
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
            "Please use the following units:",
            "- For solid ingredients: 'grams'",
            "- For liquids: 'milliliters'",
            "- For countable items: 'pieces'",
            "**Important Instructions:**",
            "Ensure that amounts are provided in these units.",
            "Do not include markdown, explanations, or comments.",
            "Please return only valid JSON and no additional text."
    })
    static class CreateRecipePrompt{
        private String ingredients;
        private int numberOfRecipes;
    }
}
