package usfca.pyne.cs601.virtualfridge.Service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService implements RecipeServiceInterface {

    private final ChatLanguageModel chatLanguageModel;

    public RecipeService (ChatLanguageModel chatLanguageModel){
        this.chatLanguageModel = chatLanguageModel;
    }

    public String generateRecipe(String ingredients){
        CreateRecipePrompt createRecipePrompt = new CreateRecipePrompt();
        createRecipePrompt.ingredients = ingredients;
        createRecipePrompt.numberOfRecipes = 5;
        Prompt prompt = StructuredPromptProcessor.toPrompt(createRecipePrompt);

        try{
            System.out.println("Querying the api...");
            AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
            String aiString = aiMessage.text();
            return aiString;
        } catch (Exception e) {
            System.out.println(e);
        }
        return ("Failed to fetch response.");
    }


    @StructuredPrompt({
            "Create {{numberOfRecipes}} recipes that can be prepared using the following ingredients: {{ingredients}}.",
            "",
            "You do not need to use all of the ingredients, just generate the best possible recipes.",
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
            "Ensure that amounts are provided in these units.",
            "Please return only valid JSON and no additional text."
    })
    static class CreateRecipePrompt{
        private String ingredients;
        private int numberOfRecipes;
    }
}
