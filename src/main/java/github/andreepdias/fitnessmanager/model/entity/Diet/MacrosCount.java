package github.andreepdias.fitnessmanager.model.entity.Diet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MacrosCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    private Double caloriesCount;
    private Double carbsCount;
    private Double proteinsCount;
    private Double fatsCount;

    @Getter
    @OneToOne
    @JoinColumn
    private DailyMeal dailyMeal;

    @Getter
    @OneToOne
    @JoinColumn
    private UserMealInfo userMealInfo;

    @Getter
    @OneToOne
    @JoinColumn
    private MealEntry mealEntry;

    public Double getCaloriesCount(){
        return (mealEntry != null) ? calculateCalories() : caloriesCount;
    }

    public Double getCarbsCount(){
        return (mealEntry != null) ? calculateCarbs() : carbsCount;
    }

    public Double getProteinsCount(){
        return (mealEntry != null) ? calculateProteins() : proteinsCount;
    }

    public Double getFatsCount(){
        return (mealEntry != null) ? calculateFats() : fatsCount;
    }

    private Double calculateCalories() {
        if(this.mealEntry.getFood() != null){
            return calculateEntryFoodMacro(this.mealEntry.getFood(), this.mealEntry.getFood().getCalories());
        }else if(this.mealEntry.getRecipe() != null){
            return calculateEntryRecipeCalories();
        }
        return 0.0;
    }

    private Double calculateCarbs() {
        if(this.mealEntry.getFood() != null){
            return calculateEntryFoodMacro(this.mealEntry.getFood(), this.mealEntry.getFood().getCarbohydrates());
        }else if(this.mealEntry.getRecipe() != null){
            return calculateEntryRecipeCarbs();
        }
        return 0.0;
    }

    private Double calculateProteins() {
        if(this.mealEntry.getFood() != null){
            return calculateEntryFoodMacro(this.mealEntry.getFood(), this.mealEntry.getFood().getProteins());
        }else if(this.mealEntry.getRecipe() != null){
            return calculateEntryRecipeProteins();
        }
        return 0.0;
    }

    private Double calculateFats() {
        if(this.mealEntry.getFood() != null){
            return calculateEntryFoodMacro(this.mealEntry.getFood(), this.mealEntry.getFood().getFats());
        }else if(this.mealEntry.getRecipe() != null){
            return calculateEntryRecipeFats();
        }
        return 0.0;
    }

    private Double calculateEntryFoodMacro(Food food, Double macro) {
        if(food.getServing() == 0) return 0.0;
        return (macro / food.getServing()) * this.mealEntry.getQuantity();
    }

    private Double calculateEntryRecipeCalories() {
        Double calories = 0.0;
        for (Ingredient ingredient : this.mealEntry.getRecipe().getIngredients()) {
            calories += (ingredient.getFood().getCalories() / ingredient.getFood().getServing()) * ingredient.getQuantity();
        }
        return (calories / this.mealEntry.getRecipe().getServing()) * this.mealEntry.getQuantity();
    }

    private Double calculateEntryRecipeCarbs() {
        Double carbs = 0.0;
        for (Ingredient ingredient : this.mealEntry.getRecipe().getIngredients()) {
            carbs += (ingredient.getFood().getCarbohydrates() / ingredient.getFood().getServing()) * ingredient.getQuantity();
        }
        return (carbs / this.mealEntry.getRecipe().getServing()) * this.mealEntry.getQuantity();
    }

    private Double calculateEntryRecipeProteins() {
        Double proteins = 0.0;
        for (Ingredient ingredient : this.mealEntry.getRecipe().getIngredients()) {
            proteins += (ingredient.getFood().getProteins() / ingredient.getFood().getServing()) * ingredient.getQuantity();
        }
        return (proteins / this.mealEntry.getRecipe().getServing()) * this.mealEntry.getQuantity();
    }

    private Double calculateEntryRecipeFats() {
        Double fats = 0.0;
        for (Ingredient ingredient : this.mealEntry.getRecipe().getIngredients()) {
            fats += (ingredient.getFood().getFats() / ingredient.getFood().getServing()) * ingredient.getQuantity();
        }
        return (fats / this.mealEntry.getRecipe().getServing()) * this.mealEntry.getQuantity();
    }

    public void addMacros(MacrosCount macrosCount) {
        Double calories = getCaloriesCount();
        Double carbs = getCarbsCount();
        Double proteins = getProteinsCount();
        Double fats = getFatsCount();

        Double addCalories = macrosCount.getCaloriesCount();
        Double addCarbs = macrosCount.getCarbsCount();
        Double addProteins = macrosCount.getProteinsCount();
        Double addFats = macrosCount.getFatsCount();

        setCaloriesCount(calories + addCalories);
        setCarbsCount(carbs + addCarbs);
        setProteinsCount(proteins + addProteins);
        setFatsCount(fats + addFats);
    }

    public void subtractMacros(MacrosCount macrosCount) {
        setCaloriesCount(getCaloriesCount() - macrosCount.getCaloriesCount());
        setCarbsCount(getCarbsCount() - macrosCount.getCarbsCount());
        setProteinsCount(getProteinsCount() - macrosCount.getProteinsCount());
        setFatsCount(getFatsCount() - macrosCount.getFatsCount());
    }
}
