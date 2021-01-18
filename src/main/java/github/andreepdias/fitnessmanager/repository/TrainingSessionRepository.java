package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Training.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {

    List<TrainingSession> findAllByTrainingRoutineIdOrderByOrderNumber(Integer trainingRoutineId);

    @Transactional
    void deleteAllByTrainingRoutineId(Integer trainingRoutineId);

    @Transactional
    void deleteByIdIn(List<Integer> toDeleteIds);
}
