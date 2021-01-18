package github.andreepdias.fitnessmanager.model.entity.Training;

import github.andreepdias.fitnessmanager.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Exercise> exercises;

    @ManyToOne
    @JoinColumn
    private User user;
}
