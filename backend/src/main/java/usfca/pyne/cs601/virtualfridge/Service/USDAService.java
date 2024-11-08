package usfca.pyne.cs601.virtualfridge.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import usfca.pyne.cs601.virtualfridge.Model.NutritionalInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class USDAService {
    private static final String API_BASE_URL = "https://api.nal.usda.gov/fdc/v1/foods/search";

    @Value("${usda.api.key}")
    private String apiKey;

    private final Gson gson = new Gson();

    public NutritionalInfo getNutritionalInfo(String ingredientName) throws IOException {
        String encodedIngredient = URLEncoder.encode(ingredientName, "UTF-8");
        String searchUrl = API_BASE_URL + "?query=" + encodedIngredient + "&pageSize=1&api_key=" + apiKey;
        URL url = new URL(searchUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to fetch data from USDA API. HTTP response code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();

        JsonObject rootObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        JsonArray foods = rootObject.getAsJsonArray("foods");

        if (foods.size() > 0) {
            JsonObject food = foods.get(0).getAsJsonObject();
            JsonArray nutrients = food.getAsJsonArray("foodNutrients");
            Double calories = null;
            Double protein = null;
            Double carbs = null;
            Double fat = null;

            for (int i = 0; i < nutrients.size(); i++) {
                JsonObject nutrient = nutrients.get(i).getAsJsonObject();
                String nutrientName = nutrient.get("nutrientName").getAsString();
                Double value = nutrient.get("value").getAsDouble();

                if (nutrientName.equalsIgnoreCase("Energy")) {
                    calories = value;
                } else if (nutrientName.equalsIgnoreCase("Protein")) {
                    protein = value;
                } else if (nutrientName.equalsIgnoreCase("Carbohydrate, by difference")) {
                    carbs = value;
                } else if (nutrientName.equalsIgnoreCase("Total lipid (fat)")) {
                    fat = value;
                }
            }

            NutritionalInfo info = new NutritionalInfo();
            info.setCalories(calories != null ? calories : 0.0);
            info.setProtein(protein != null ? protein : 0.0);
            info.setCarbs(carbs != null ? carbs : 0.0);
            info.setFat(fat != null ? fat : 0.0);

            return info;
        } else {
            throw new RuntimeException("No data found for ingredient: " + ingredientName);
        }
    }
}
