package github.andreepdias.fitnessmanager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRoutineDTO {

    private Integer id;
    private String name;
    private String description;
    private String goal;
    private Integer weekFrequency;
    private boolean isActiveRoutine;
    private UserDTO user;
    private List<TrainingSessionDTO> trainingSessions = new ArrayList<>();

    @JsonProperty("isActiveRoutine")
    public boolean getIsActiveRoutine() {
        return this.isActiveRoutine;
    }
}
