package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Diet.Ingredient;
import github.andreepdias.fitnessmanager.model.entity.Diet.Recipe;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.repository.IngredientRepository;
import github.andreepdias.fitnessmanager.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository repository;
    private final IngredientRepository ingredientRepository;

    private final IngredientService ingredientService;
    private final MealService mealService;
    private final UserService userService;

    public List<Recipe> findAll(){
        Integer userId = userService.findAuthenticatedUser().getId();
        List<Recipe> recipes = repository.findAllByUserIdOrderByNameAsc(userId);
        return recipes;
    }

    public Page<Recipe> findPage(PageRequest pageRequest) {
        Integer userId = userService.findAuthenticatedUser().getId();
        Page<Recipe> recipes = repository.findAllByUserIdOrderByNameAsc(pageRequest, userId);
        return recipes;
    }

    public Recipe findById(Integer id) {
        Integer userId = userService.findAuthenticatedUser().getId();
        Recipe recipe = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with id " + id + " was not found."));
        return recipe;
    }

    public boolean existsByIdAndUserId(Integer id){
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.existsByIdAndUserId(id, userId);
    }

    public Recipe create(Recipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredients();

        User user = userService.findAuthenticatedUser();

        recipe.setId(null);
        recipe.setIngredients(null);
        recipe.setUser(user);
        recipe = repository.save(recipe);

        ingredients = createIngredients(ingredients, recipe);

        recipe.setIngredients(ingredients);
        return repository.save(recipe);

    }

    private List<Ingredient> createIngredients(List<Ingredient> ingredients, Recipe recipe) {
        for (Ingredient ingredient : ingredients) {
            ingredient.setId(null);
            ingredient.setRecipe(recipe);
        }
        return ingredientRepository.saveAll(ingredients);
    }

    public Recipe update(Recipe recipe) {
        Recipe oldRecipe = findById(recipe.getId());
        updateRecipeData(oldRecipe, recipe);
        return repository.save(oldRecipe);
    }

    private void updateRecipeData(Recipe oldRecipe, Recipe newRecipe) {
        oldRecipe.setName(newRecipe.getName());
    }

    public void deleteById(Integer id) {
        if(!existsByIdAndUserId(id)){
            throw new ObjectNotFoundException("Recipe with id " + id + " was not found.");
        }
        boolean existsMealEntriesAssociated = mealService.existsByRecipeId(id);
        if(existsMealEntriesAssociated){
            throw new DataInconsistencyException("You can't delete a recipe if it's part of a daily meal.");
        }
        ingredientService.deleteAllByRecipeId(id);
        repository.deleteById(id);
    }

    public List<Ingredient> findIngredientsByRecipeId(Integer recipeId) {
        return ingredientRepository.findAllByRecipeId(recipeId);
    }
}
