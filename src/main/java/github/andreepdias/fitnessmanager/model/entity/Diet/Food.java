package github.andreepdias.fitnessmanager.model.entity.Diet;

import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.model.entity.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Double serving;

    @Column
    private Unit unit;

    @Column
    private Double carbohydrates;

    @Column
    private Double proteins;

    @Column
    private Double fats;

    @Column
    private Double calories;

    @ManyToOne
    @JoinColumn
    private User user;
}
