package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.*;
import github.andreepdias.fitnessmanager.model.entity.Diet.*;
import github.andreepdias.fitnessmanager.model.entity.enums.Unit;
import github.andreepdias.fitnessmanager.service.MealService;
import github.andreepdias.fitnessmanager.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DailyMealMapper {

    private final ModelMapper mapper;

    private final MealService service;
    private final RecipeService recipeService;

    public DailyMeal toEntity(DailyMealDTO dto){
        return new DailyMeal();
    }

    public DailyMealDTO toDTO(DailyMeal dailyMeal){
        DailyMealDTO dailyMealDTO = mapper.map(dailyMeal, DailyMealDTO.class);

        List<MealDTO> mealsDTO = populateMealsDTO(dailyMeal, dailyMealDTO);

        populateMealEntriesDTO(mealsDTO);
        return dailyMealDTO;
    }

    private void populateMealEntriesDTO(List<MealDTO> mealsDTO) {
        for (MealDTO mealDTO : mealsDTO) {
            List<MealEntry> mealEntries = service.findMealEntriesByMealId(mealDTO.getId());
            List<MealEntryDTO> mealEntriesDTO = mealEntriesToDTO(mealEntries, mealDTO.getId());
            mealDTO.setMealEntries(mealEntriesDTO);


        }
    }

    private List<MealDTO> populateMealsDTO(DailyMeal dailyMeal, DailyMealDTO dailyMealDTO) {
        List<Meal> meals = service.findMealsByDailyMealId(dailyMeal.getId());
        List<MealDTO> mealsDTO = meals.stream().map(x -> mapper.map(x, MealDTO.class)).collect(Collectors.toList());
        dailyMealDTO.setMeals(mealsDTO);
        return mealsDTO;
    }

    private List<MealEntryDTO> mealEntriesToDTO(List<MealEntry> mealEntries, Integer mealId) {
        List<MealEntryDTO> mealEntriesDTO = new ArrayList<>();

        for (MealEntry mealEntry : mealEntries) {
            MealEntryDTO mealEntryDTO = mapper.map(mealEntry, MealEntryDTO.class);
            mealEntryDTO.setMealId(mealId);

            if(mealEntry.getFood() != null){
                mealEntryDTO.getFood().setUnit(mealEntry.getFood().getUnit().getDescription());
            }
            if(mealEntry.getRecipe() != null){
                mealEntryDTO.getRecipe().setUnit(mealEntry.getRecipe().getUnit().getDescription());
            }

            mealEntriesDTO.add(mealEntryDTO);

            insertRecipeIngredients(mealEntryDTO);
        }
        return mealEntriesDTO;
    }

    private void insertRecipeIngredients(MealEntryDTO mealEntryDTO) {
        if (mealEntryDTO.getRecipe() != null) {
            List<Ingredient> ingredients = recipeService.findIngredientsByRecipeId(mealEntryDTO.getRecipe().getId());
            List<IngredientDTO> ingredientDTOS = ingredients.stream()
                                                    .map(x -> mapper.map(x, IngredientDTO.class))
                                                    .collect(Collectors.toList());
            mealEntryDTO.getRecipe().setIngredients(ingredientDTOS);
        }
    }

    public MealEntry toEntryEntity(MealEntryDTO dto) {
        Meal meal = service.findMealById(dto.getMealId());
        DailyMeal dailyMeal = service.findDailyMealById(dto.getDailyMealId());

        MealEntry entry = mapper.map(dto, MealEntry.class);
        entry.setMeal(meal);
        entry.setDailyMeal(dailyMeal);

        if(dto.getFood() != null){
            entry.getFood().setUnit(Unit.toEnum(dto.getFood().getUnit()));
        }
        if(dto.getRecipe() != null){
            entry.getRecipe().setUnit(Unit.toEnum(dto.getRecipe().getUnit()));
        }

        return entry;
    }

    public MealEntryDTO toEntryDTO(MealEntry entry) {
        MealEntryDTO dto = mapper.map(entry, MealEntryDTO.class);
        dto.setMealId(entry.getMeal().getId());

        if(entry.getFood() != null){
            dto.getFood().setUnit(entry.getFood().getUnit().getDescription());
        }
        if(entry.getRecipe() != null){
            dto.getRecipe().setUnit(entry.getRecipe().getUnit().getDescription());
        }
        insertRecipeIngredients(dto);

        return dto;
    }

    public DailyMealDTO newDailyMealToDTO(DailyMeal dailyMeal, List<Meal> meals) {
        DailyMealDTO dto = mapper.map(dailyMeal, DailyMealDTO.class);

        List<MealDTO> mealsDTO = meals.stream()
                                    .map(x -> mapper.map(x, MealDTO.class))
                                    .collect(Collectors.toList());
        dto.setMeals(mealsDTO);
        return dto;
    }

    public UserMealInfo toMealInfoEntity(UserMealInfoDTO dto) {
        UserMealInfo entity = mapper.map(dto, UserMealInfo.class);
        entity.setMealsPerDay(dto.getMealsName().size());
        return entity;
    }

    public UserMealInfoDTO toMealInfoDTO(UserMealInfo userMealInfo) {
        UserMealInfoDTO dto = mapper.map(userMealInfo, UserMealInfoDTO.class);

        List<MealName> mealNames = service.findMealNamesActiveUser();
        List<MealNameDTO> mealNamesDTO = mealNames.stream().map(x -> mapper.map(x, MealNameDTO.class)).collect(Collectors.toList());
        dto.setMealsName(mealNamesDTO);

        return dto;
    }

    public List<MealName> toMealNameEntity(List<MealNameDTO> mealsNameDTO) {
        return mealsNameDTO.stream().map(x -> mapper.map(x, MealName.class)).collect(Collectors.toList());
    }

    public MacrosCount toMacrosCountEntity(MacrosCountDTO dto) {
        return mapper.map(dto, MacrosCount.class);
    }
}
