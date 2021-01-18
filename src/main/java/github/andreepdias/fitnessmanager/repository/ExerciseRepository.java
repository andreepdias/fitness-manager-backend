package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Training.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    boolean existsByTags_Id(Integer exerciseId);

    List<Exercise> findAllByUserId(Integer userId);
    Page<Exercise> findAllByUserId(Pageable pageable, Integer userId);
}
