package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MealEntryDTO {
    private Integer id;
    private Integer mealId;
    private String quantity;
    private FoodDTO food;
    private RecipeDTO recipe;
    private MacrosCountDTO macrosCount;
    private Integer dailyMealId;
}
