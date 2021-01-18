package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Diet.Food;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository repository;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final MealService mealService;
    private final UserService userService;

    public List<Food> findAll(){
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserIdOrderByNameAsc(userId);
    }

    public Page<Food> findPage(PageRequest pageRequest) {
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserIdOrderByNameAsc(pageRequest, userId);
    }

    public boolean existsByIdAndUserId(Integer id, Integer userId){
        return repository.existsByIdAndUserId(id, userId);
    }

    public Food findByIdAndUserId(Integer foodId, Integer userId) {
        return repository.findByIdAndUserId(foodId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Food with id " + foodId + " was not found."));
    }

    public Food create(Food food) {
        User user = userService.findAuthenticatedUser();
        food.setId(null);
        food.setUser(user);
        return repository.save(food);
    }

    public void update(Food food) {
        Integer userId = userService.findAuthenticatedUser().getId();
        Food oldFood = findByIdAndUserId(food.getId(), userId);
        updateFoodData(oldFood, food);
        repository.save(oldFood);
    }

    private void updateFoodData(Food oldFood, Food food) {
        oldFood.setName(food.getName());
        oldFood.setServing(food.getServing());
        oldFood.setUnit(food.getUnit());
        oldFood.setCarbohydrates(food.getCarbohydrates());
        oldFood.setCalories(food.getCalories());
        oldFood.setProteins(food.getProteins());
        oldFood.setFats(food.getFats());
    }

    public List<Food> getByListId(List<Integer> foodsIdList) {
        return repository.findByIdIn(foodsIdList);
    }

    public void deleteById(Integer id) {
        Integer userId = userService.findAuthenticatedUser().getId();
        if(!existsByIdAndUserId(id, userId)){
            throw new ObjectNotFoundException("Food with id " + id + " was not found.");
        }
        boolean existsRecipesAssociated = ingredientService.existsByFoodId(id);
        if(existsRecipesAssociated){
           throw new DataInconsistencyException("You can't delete a food with recipes associated.");
        }
        boolean existsEntriesAssociated = mealService.existsEntryByFoodId(id);
        if(existsEntriesAssociated){
            throw new DataInconsistencyException("You can't delete a food if it is part of any daily meal.");
        }
        repository.deleteById(id);
    }

    public Food findByIdAndAuthenticatedUser(Integer id) {
        Integer userId = userService.findAuthenticatedUser().getId();
        return findByIdAndUserId(id, userId);
    }
}
