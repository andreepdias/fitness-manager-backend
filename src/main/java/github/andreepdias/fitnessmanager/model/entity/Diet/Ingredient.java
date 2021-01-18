package github.andreepdias.fitnessmanager.model.entity.Diet;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double quantity;

    @ManyToOne
    @JoinColumn
    private Food food;

    @ManyToOne
    @JoinColumn
    private Recipe recipe;
}
