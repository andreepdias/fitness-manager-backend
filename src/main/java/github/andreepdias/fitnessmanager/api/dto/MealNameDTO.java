package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealNameDTO {
    private Integer id;
    private Integer orderNumber;
    private String name;
    private UserDTO user;
}
