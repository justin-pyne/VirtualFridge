package usfca.pyne.cs601.virtualfridge.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalorieCalcDTO {
    private String gender;
    private Double weightKg;
    private Double heightCm;
    private Integer age;
    private String activityLevel;
}
