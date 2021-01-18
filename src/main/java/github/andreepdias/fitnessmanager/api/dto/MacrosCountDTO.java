package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MacrosCountDTO {

    private Integer id;
    private String caloriesCount;
    private String carbsCount;
    private String proteinsCount;
    private String fatsCount;

}
