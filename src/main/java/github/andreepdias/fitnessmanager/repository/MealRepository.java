package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Diet.Meal;
import github.andreepdias.fitnessmanager.model.entity.Diet.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    List<Meal> findAllByDailyMealId(Integer dailyMealId);

    @Transactional
    void deleteByIdIn(List<Integer> toDeleteMealsId);
}
