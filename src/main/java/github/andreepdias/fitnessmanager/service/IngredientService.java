package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.model.entity.Diet.Ingredient;
import github.andreepdias.fitnessmanager.model.entity.Diet.Recipe;
import github.andreepdias.fitnessmanager.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IngredientService {

    private final IngredientRepository repository;

    public List<Ingredient> findAllByRecipeId(Integer recipeId) {
        return repository.findAllByRecipeId(recipeId);
    }

    public List<Ingredient> createAll(List<Ingredient> ingredients, Recipe recipe) {
        for (Ingredient ingredient : ingredients) {
            ingredient.setId(null);
            ingredient.setRecipe(recipe);
        }
        return repository.saveAll(ingredients);
    }

    public void updateIngredients(List<Ingredient> newIngredients, Recipe recipe) {
        List<Ingredient> oldIngredients = findAllByRecipeId(recipe.getId());

        List<Integer> toDeleteIds = oldIngredients.stream()
                                        .filter(x -> !newIngredients.contains(x.getId()))
                                        .map(x -> x.getId())
                                        .collect(Collectors.toList());
        deleteAllById(toDeleteIds);

        for (Ingredient newIngredient : newIngredients) {
            newIngredient.setRecipe(recipe);
        }
        saveAll(newIngredients);
    }

    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        return repository.saveAll(ingredients);
    }

    public void deleteAllById(List<Integer> toDeleteIngredientsId) {
        repository.deleteByIdIn(toDeleteIngredientsId);
    }

    public void deleteAllByRecipeId(Integer recipeId) {
        repository.deleteAllByRecipeId(recipeId);
    }

    public boolean existsByFoodId(Integer foodId) {
        return repository.existsByFoodId(foodId);
    }

}
