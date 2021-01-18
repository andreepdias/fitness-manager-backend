package github.andreepdias.fitnessmanager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSessionDTO {

    private Integer id;
    private Integer orderNumber;
    private String description;
    private boolean isCurrentSession;
    private List<SessionExerciseDTO> sessionExercises = new ArrayList<>();
    private Integer trainingRoutineId;

    @JsonProperty("isCurrentSession")
    public boolean getIsCurrentSession() {
        return this.isCurrentSession;
    }

}
