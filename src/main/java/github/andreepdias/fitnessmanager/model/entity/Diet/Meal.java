package github.andreepdias.fitnessmanager.model.entity.Diet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String mealName;

    @Column
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn
    private DailyMeal dailyMeal;
}
