package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMealDTO {

    private Integer id;
    private String date;
    private UserDTO user;
    private MacrosCountDTO macrosCount;
    private List<MealDTO> meals = new ArrayList<>();

}
