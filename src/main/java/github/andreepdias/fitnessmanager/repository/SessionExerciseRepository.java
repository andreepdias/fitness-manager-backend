package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Training.SessionExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SessionExerciseRepository extends JpaRepository<SessionExercise, Integer> {
    List<SessionExercise> findAllByTrainingSessionId(Integer trainingSessionId);

    @Transactional
    void deleteAllByTrainingSessionId(Integer trainingSessionId);

    boolean existsByExerciseId(Integer exerciseId);
}
