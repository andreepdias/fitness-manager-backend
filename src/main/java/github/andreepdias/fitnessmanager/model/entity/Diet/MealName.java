package github.andreepdias.fitnessmanager.model.entity.Diet;

import github.andreepdias.fitnessmanager.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer orderNumber;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn
    private User user;

}
