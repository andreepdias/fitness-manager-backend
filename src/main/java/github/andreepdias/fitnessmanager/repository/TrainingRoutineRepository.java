package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Training.TrainingRoutine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRoutineRepository extends JpaRepository<TrainingRoutine, Integer> {

    List<TrainingRoutine> findAllByUserId(Integer userId);
    Page<TrainingRoutine> findAllByUserId(Pageable pageRequest, Integer userId);
}
