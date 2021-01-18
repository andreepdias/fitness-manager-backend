package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDTO {

    private Integer id;
    private String name;
    private String serving;
    private String unit;
    private String carbohydrates;
    private String proteins;
    private String fats;
    private String calories;
}
