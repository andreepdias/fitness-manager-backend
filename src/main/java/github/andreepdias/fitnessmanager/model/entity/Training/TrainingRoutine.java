package github.andreepdias.fitnessmanager.model.entity.Training;

import github.andreepdias.fitnessmanager.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private String goal;
    private Integer weekFrequency;
    private boolean isActiveRoutine;

    @ManyToOne
    @JoinColumn
    private User user;

}
