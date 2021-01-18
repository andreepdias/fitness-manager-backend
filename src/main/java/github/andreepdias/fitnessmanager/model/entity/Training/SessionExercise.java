package github.andreepdias.fitnessmanager.model.entity.Training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private Exercise exercise;

    @Column
    private Integer sets;

    @Column
    private Integer repetitions;

    @Column
    private Double restInterval;

    @ManyToOne
    @JoinColumn
    private TrainingSession trainingSession;
}
