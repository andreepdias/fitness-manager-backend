package github.andreepdias.fitnessmanager.model.entity.Diet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class MealEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double quantity;

    @ManyToOne
    @JoinColumn
    private Meal meal;

    @ManyToOne
    @JoinColumn
    private Food food;

    @ManyToOne
    @JoinColumn
    private Recipe recipe;

    @OneToOne(mappedBy = "mealEntry")
    private MacrosCount macrosCount;

    @ManyToOne
    @JoinColumn
    private DailyMeal dailyMeal;

}
