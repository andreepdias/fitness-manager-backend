package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Diet.*;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MealService {

    private final DailyMealRepository dailyMealRepository;
    private final MealRepository mealRepository;
    private final MealEntryRepository mealEntryRepository;
    private final MealNameRepository mealNameRepository;
    private final UserMealInfoRepository userMealInfoRepository;
    private final MacrosCountRepository macrosCountRepository;
    private final UserService userService;
    private final IngredientRepository ingredientRepository;

    public DailyMeal findByDate(Integer day, Integer month, Integer year){
        LocalDate date = LocalDate.of(year, month, day);
        Integer userId = userService.findAuthenticatedUser().getId();

        List<DailyMeal> dailyMealList = dailyMealRepository.findByDateAndUserId(date, userId);

        if(dailyMealList.size() > 0){
            return dailyMealList.get(0);
        }

        MacrosCount macrosCount = new MacrosCount(null, 0.0, 0.0, 0.0, 0.0, null, null, null);
        return new DailyMeal(null, null, macrosCount, null);
    }

    public DailyMeal createDailyMealByDate(Integer day, Integer month, Integer year, User user) {
        MacrosCount macrosCount = new MacrosCount(null, 0.0, 0.0, 0.0, 0.0, null, null, null);
        macrosCountRepository.save(macrosCount);

        DailyMeal dailyMeal = new DailyMeal(null, LocalDate.of(year, month, day), macrosCount, user);
        dailyMeal = dailyMealRepository.save(dailyMeal);

        macrosCount.setDailyMeal(dailyMeal);
        macrosCountRepository.save(macrosCount);

        return dailyMeal;
    }

    public List<Meal> findMealsByDailyMealId(Integer dailyMealId) {
        return mealRepository.findAllByDailyMealId(dailyMealId);
    }

    public Meal findMealById(Integer mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new ObjectNotFoundException("Meal with id " + mealId + " was not found."));
    }

    public List<MealEntry> findMealEntriesByMealId(Integer mealId) {
        return mealEntryRepository.findAllByMealId(mealId);
    }

    public MealEntry createEntry(MealEntry entry) {
        MacrosCount macrosCount = new MacrosCount(null, 0.0, 0.0, 0.0, 0.0, null, null, null);
        macrosCountRepository.save(macrosCount);

        entry.setId(null);
        entry.setMacrosCount(macrosCount);
        entry = mealEntryRepository.save(entry);

        macrosCount.setMealEntry(entry);
        macrosCountRepository.save(macrosCount);

        addDailyMealMacrosCount(entry);

        return entry;
    }

    public MealEntry updateEntry(MealEntry entry) {
        MealEntry oldEntry = findEntryById(entry.getId());

        subtractDailyMealMacrosCount(oldEntry);
        updateMealEntryData(oldEntry, entry);
        addDailyMealMacrosCount(oldEntry);

        return mealEntryRepository.save(oldEntry);
    }

    private void addDailyMealMacrosCount(MealEntry mealEntry) {
        MacrosCount macrosCurrent = mealEntry.getDailyMeal().getMacrosCount();
        MacrosCount macrosCount = mealEntry.getMacrosCount();
        macrosCurrent.addMacros(macrosCount);
        macrosCountRepository.save(macrosCurrent);
    }

    private void subtractDailyMealMacrosCount(MealEntry mealEntry) {
        MacrosCount macrosCurrent = mealEntry.getDailyMeal().getMacrosCount();
        MacrosCount macrosCount = mealEntry.getMacrosCount();
        macrosCurrent.subtractMacros(macrosCount);
        macrosCountRepository.save(macrosCurrent);
    }

    private void updateMealEntryData(MealEntry oldEntry, MealEntry newEntry) {
        oldEntry.setQuantity(newEntry.getQuantity());
        oldEntry.setFood(newEntry.getFood());
        oldEntry.setRecipe(newEntry.getRecipe());

    }

    private MealEntry findEntryById(Integer id) {
        return mealEntryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Meal entry with id " + id + " was not found."));
    }

    public void deleteEntryById(Integer entryId) {
        MealEntry mealEntry = findEntryById(entryId);

        subtractDailyMealMacrosCount(mealEntry);

        macrosCountRepository.deleteAllByMealEntryId(entryId);
        mealEntryRepository.deleteById(entryId);
    }

    public List<Meal> createMeals(DailyMeal dailyMeal, User user) {
        List<MealName> mealNames = findMealNamesByUser(user);

        List<Meal> meals = new ArrayList<>();
        for (MealName mealName : mealNames) {
            Meal meal = new Meal(null, mealName.getName(), mealName.getOrderNumber(), dailyMeal);
            meals.add(meal);
        }
        return mealRepository.saveAll(meals);
    }

    private List<MealName> findMealNamesByUser(User user) {
        return mealNameRepository.findAllByUserIdOrderByOrderNumberAsc(user.getId());
    }

    public boolean existsEntryByFoodId(Integer foodId) {
        return mealEntryRepository.existsByFoodId(foodId);
    }

    public UserMealInfo updateMealInfo(UserMealInfo mealInfo) {
        UserMealInfo oldMealInfo = findMealInfoById(mealInfo);
        updateMealInfoData(oldMealInfo, mealInfo);
        return userMealInfoRepository.save(oldMealInfo);
    }

    private void updateMealInfoData(UserMealInfo oldMealInfo, UserMealInfo mealInfo) {
        oldMealInfo.setMealsPerDay(mealInfo.getMealsPerDay());
    }

    private UserMealInfo findMealInfoById(UserMealInfo mealInfo) {
        return userMealInfoRepository.findById(mealInfo.getId())
                .orElseThrow(() -> new ObjectNotFoundException("User meal info with id " + mealInfo.getId() + " was not found."));
    }

    public UserMealInfo findMealInfoById(Integer id) {
        return userMealInfoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User meal info with id " + id + " was not found."));
    }

    public UserMealInfo findActiveUserMealInfo() {
        User user = userService.findAuthenticatedUser();
        return user.getUserMealInfo();
    }

    public List<MealName> findMealNamesActiveUser() {
        Integer userId = userService.findAuthenticatedUser().getId();
        return mealNameRepository.findAllByUserIdOrderByOrderNumberAsc(userId);
    }

    public List<MealName> updateMealNames(List<MealName> mealNames) {
        User user = userService.findAuthenticatedUser();
        for (MealName mealName : mealNames) {
            mealName.setId(null);
            mealName.setUser(user);
        }
        deleteAllMealNamesByUserId(user.getId());
        return createMealNames(mealNames);
    }

    private List<MealName> createMealNames(List<MealName> mealNames) {
        return mealNameRepository.saveAll(mealNames);
    }

    private void deleteAllMealNamesByUserId(Integer userId) {
        mealNameRepository.deleteAllByUserId(userId);
    }

    public void updateDailyMealNames(Integer day, Integer month, Integer year, List<MealName> newMealNames) {
        DailyMeal dailyMeal = findByDate(day, month, year);

        if(dailyMeal.getId() != null){
            List<Meal> meals = findMealsByDailyMealId(dailyMeal.getId());
            updateMealsWithNames(meals, newMealNames, dailyMeal);
        }
    }

    private void updateMealsWithNames(List<Meal> meals, List<MealName> mealNames, DailyMeal dailyMeal) {

        Map<Integer, MealName> mealNameMap = new HashMap<>();

        for (MealName mealName : mealNames) {
            mealNameMap.put(mealName.getOrderNumber(), mealName);
        }

        List<Meal> toSaveMeals = new ArrayList<>();
        List<Integer> toDeleteMealsId = new ArrayList<>();

        for (Meal meal : meals) {
            if(mealNameMap.containsKey(meal.getOrderNumber())){
                meal.setMealName(mealNameMap.get(meal.getOrderNumber()).getName() );
                toSaveMeals.add(meal);
            }else{
                toDeleteMealsId.add(meal.getId());
            }
        }

        if(toSaveMeals.size() < mealNames.size()){
            for(int i = toSaveMeals.size() + 1; i <= mealNames.size(); i++){
                MealName mealName = mealNameMap.get(i);
                Meal newMeal = new Meal(null, mealName.getName(), mealName.getOrderNumber(), dailyMeal);
                toSaveMeals.add(newMeal);
            }
        }

        deleteAllMealsById(toDeleteMealsId);
        updateMeals(toSaveMeals);
    }

    private void updateMeals(List<Meal> toUpdateMeals) {
        mealRepository.saveAll(toUpdateMeals);
    }

    private void deleteAllMealsById(List<Integer> toDeleteMealsId) {

        List<MealEntry> mealEntries = findAllMealEntryByMealIdIn(toDeleteMealsId);
        subtractDailyMealAllMacrosCount(mealEntries);

        mealRepository.deleteByIdIn(toDeleteMealsId);
    }

    private void subtractDailyMealAllMacrosCount(List<MealEntry> mealEntries) {
        if(mealEntries.size() == 0) return;

        MacrosCount macrosCurrent = mealEntries.get(0).getDailyMeal().getMacrosCount();
        for (MealEntry mealEntry : mealEntries) {
            MacrosCount macrosCount = mealEntry.getMacrosCount();
            macrosCurrent.subtractMacros(macrosCount);
        }
        macrosCountRepository.save(macrosCurrent);
    }

    private List<MealEntry> findAllMealEntryByMealIdIn(List<Integer> toDeleteMealsId) {
        return mealEntryRepository.findByMealIdIn(toDeleteMealsId);
    }

    public void updateMacrosCount(MacrosCount macrosCount) {
        MacrosCount oldMacrosCount = findMacrosCountById(macrosCount.getId());
        updateMacrosCountData(oldMacrosCount, macrosCount);
        macrosCountRepository.save(oldMacrosCount);
    }

    private void updateMacrosCountData(MacrosCount oldMacrosCount, MacrosCount macrosCount) {
        oldMacrosCount.setCaloriesCount(macrosCount.getCaloriesCount());
        oldMacrosCount.setCarbsCount(macrosCount.getCarbsCount());
        oldMacrosCount.setProteinsCount(macrosCount.getProteinsCount());
        oldMacrosCount.setFatsCount(macrosCount.getFatsCount());

    }

    public MacrosCount findMacrosCountById(Integer id) {
        return macrosCountRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Macros count with id " + id + " was not found."));
    }

    public DailyMeal findDailyMealById(Integer dailyMealId) {
        return dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new ObjectNotFoundException("Daily meal with id " + dailyMealId + " was not found."));
    }

    public boolean existsByRecipeId(Integer recipeId) {
        return mealEntryRepository.existsByRecipeId(recipeId);
    }
}
