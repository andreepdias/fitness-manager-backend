package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealDTO {

    private Integer id;
    private String mealName;
    private Integer orderNumber;
    private List<MealEntryDTO> mealEntries = new ArrayList<>();
}
