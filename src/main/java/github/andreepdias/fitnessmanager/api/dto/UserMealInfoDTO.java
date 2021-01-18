package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMealInfoDTO {
    private Integer id;
    private String mealsPerDay;
    private UserDTO user;
    private MacrosCountDTO macrosGoal;
    private List<MealNameDTO> mealsName = new ArrayList<>();

}
