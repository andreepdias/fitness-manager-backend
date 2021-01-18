package github.andreepdias.fitnessmanager.model.entity.Training;

import github.andreepdias.fitnessmanager.model.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTrainingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn
    private TrainingRoutine activeRoutine;

    @OneToOne
    @JoinColumn
    private User user;
}
