package github.andreepdias.fitnessmanager.model.entity.Diet;

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
public class UserMealInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer mealsPerDay;

    @OneToOne(mappedBy = "userMealInfo")
    private MacrosCount macrosGoal;

    @OneToOne
    @JoinColumn
    private User user;

}
