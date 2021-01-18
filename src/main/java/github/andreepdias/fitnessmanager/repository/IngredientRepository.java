package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Diet.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findByIdIn(List<Integer> ingredientsId);

    void deleteByIdIn(List<Integer> toDeleteIngredientsId);

    List<Ingredient> findAllByRecipeId(Integer recipeId);

    void deleteAllByRecipeId(Integer recipeId);

    boolean existsByFoodId(Integer foodId);
}
