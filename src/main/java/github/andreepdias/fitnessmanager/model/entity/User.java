package github.andreepdias.fitnessmanager.model.entity;

import github.andreepdias.fitnessmanager.model.entity.Diet.UserMealInfo;
import github.andreepdias.fitnessmanager.model.entity.Training.UserTrainingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @OneToOne(mappedBy = "user")
    private UserMealInfo userMealInfo;

    @OneToOne(mappedBy = "user")
    private UserTrainingInfo userTrainingInfo;


}
