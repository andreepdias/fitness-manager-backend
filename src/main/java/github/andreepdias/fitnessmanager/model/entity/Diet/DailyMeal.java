package github.andreepdias.fitnessmanager.model.entity.Diet;

import github.andreepdias.fitnessmanager.model.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDate date;

    @OneToOne(mappedBy = "dailyMeal")
    private MacrosCount macrosCount;

    @ManyToOne
    @JoinColumn
    private User user;
}
