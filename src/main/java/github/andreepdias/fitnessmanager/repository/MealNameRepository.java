package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Diet.MealName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MealNameRepository extends JpaRepository<MealName, Integer> {
    List<MealName> findAllByUserIdOrderByOrderNumberAsc(Integer userId);

    @Transactional
    void deleteAllByUserId(Integer userId);
}
