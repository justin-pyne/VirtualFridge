package usfca.pyne.cs601.virtualfridge.Service;

public class CalorieCalculatorService {

    public static double calculateBMR(String gender, double weightKg, double heightCm, int age) {
        if (gender.equalsIgnoreCase("male")){
            return 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
        } else {
            return 10 * weightKg + 6.25 * heightCm - 5 * age - 161;
        }
    }

    public static double calculateMaintenanceCalories(double bmr, String activityLevel) {
        double activityFactor;

        switch(activityLevel.toLowerCase()) {
            case "sedentary":
                activityFactor = 1.2;
                break;
            case "light":
                activityFactor = 1.375;
                break;
            case "moderate":
                activityFactor = 1.55;
                break;
            case "active":
                activityFactor = 1.725;
                break;
            case "very active":
                activityFactor = 1.9;
                break;
            default:
                throw new IllegalArgumentException("Invalid activity level: " + activityLevel);
        }

        return bmr * activityFactor;
    }

    public static double[] calculateMacros(double maintenanceCalories) {
        double proteinCalories = maintenanceCalories * 0.20;
        double carbsCalories = maintenanceCalories * 0.50;
        double fatCalories = maintenanceCalories * 0.30;

        double protein = proteinCalories / 4;
        double carbs = carbsCalories / 4;
        double fat = fatCalories / 9;

        return new double[]{protein, carbs, fat};
    }
}
