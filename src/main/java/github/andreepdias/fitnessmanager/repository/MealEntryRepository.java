package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Diet.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, Integer> {
    List<MealEntry> findAllByMealId(Integer mealId);

    boolean existsByFoodId(Integer foodId);

    List<MealEntry> findByMealIdIn(List<Integer> toDeleteMealsId);

    boolean existsByRecipeId(Integer recipeId);
}
