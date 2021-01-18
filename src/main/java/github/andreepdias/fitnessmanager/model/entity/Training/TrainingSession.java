package github.andreepdias.fitnessmanager.model.entity.Training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer orderNumber;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn
    private TrainingRoutine trainingRoutine;

    private boolean isCurrentSession;
}
