package usfca.pyne.cs601.virtualfridge.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MacrosDTO {
    private double dailyCalorieGoal;
    private double dailyProteinGoal;
    private double dailyCarbsGoal;
    private double dailyFatGoal;

    private double currentCalories;
    private double currentProtein;
    private double currentCarbs;
    private double currentFat;

    private double remainingCalories;
    private double remainingProtein;
    private double remainingCarbs;
    private double remainingFat;
}
