package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Training.UserTrainingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTrainingInfoRepository extends JpaRepository<UserTrainingInfo, Integer> {

//    Optional<UserTrainingInfo> findByUserId(Integer userId);
}
