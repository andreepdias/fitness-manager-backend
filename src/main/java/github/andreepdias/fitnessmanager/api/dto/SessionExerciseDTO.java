package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionExerciseDTO {

    private Integer id;
    private ExerciseDTO exercise;
    private Integer sets;
    private Integer repetitions;
    private Double restInterval;
    private Integer trainingSessionId;
}
