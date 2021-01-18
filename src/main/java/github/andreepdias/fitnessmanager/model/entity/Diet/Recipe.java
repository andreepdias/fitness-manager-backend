package github.andreepdias.fitnessmanager.model.entity.Diet;

import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.model.entity.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Double serving;

    @Column
    private Unit unit;

    @OneToMany(mappedBy = "recipe")
    private List<Ingredient> ingredients;

    @ManyToOne
    @JoinColumn
    private User user;



}
