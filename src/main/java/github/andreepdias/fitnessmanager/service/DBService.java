package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.model.entity.Diet.*;
import github.andreepdias.fitnessmanager.model.entity.Training.*;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.model.entity.enums.Unit;
import github.andreepdias.fitnessmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DBService {

    private final UserRepository userRepository;

    private final FoodRepository foodRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    private final DailyMealRepository dailyMealRepository;
    private final MealNameRepository mealNameRepository;
    private final MealRepository mealRepository;
    private final MealEntryRepository mealEntryRepository;
    private final UserMealInfoRepository userMealInfoRepository;
    private final MacrosCountRepository macrosCountRepository;

    private final TagRepository tagRepository;
    private final ExerciseRepository exerciseRepository;
    private final TrainingRoutineRepository trainingRoutineRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final SessionExerciseRepository sessionExerciseRepository;
    private final UserTrainingInfoRepository userTrainingInfoRepository;

    public void instantiateDatabase(){
        User user = new User(null, "André", "andre.dias@msn.com", "andre", null, null);
        user = userRepository.save(user);

        UserMealInfo userMealInfo = new UserMealInfo(null, 3, null, user);
        userMealInfo = userMealInfoRepository.save(userMealInfo);

        MacrosCount macrosCount = new MacrosCount(null, 2000.0, 150.0, 100.0, 50.0, null, userMealInfo, null);
        macrosCount = macrosCountRepository.save(macrosCount);

        UserTrainingInfo userTrainingInfo = new UserTrainingInfo(null, null, user);
        userTrainingInfo = userTrainingInfoRepository.save(userTrainingInfo);

        user.setUserMealInfo(userMealInfo);
        user.setUserTrainingInfo(userTrainingInfo);
        user = userRepository.save(user);

        userMealInfo.setMacrosGoal(macrosCount);
        macrosCountRepository.save(macrosCount);

        MealName mealName1 = new MealName(null, 1, "Meal 1", user);
        MealName mealName2 = new MealName(null, 2, "Meal 2", user);
        MealName mealName3 = new MealName(null, 3, "Meal 3", user);
        mealNameRepository.saveAll(Arrays.asList(mealName1, mealName2, mealName3));


        instantiateDietRelated(user);
        instantiateTrainingRelated(user);
    }

    private void instantiateTrainingRelated(User u1) {
        Tag t1 = new Tag(null, "Peitoral", null, u1);
        Tag t2 = new Tag(null, "Dorsal", null, u1);
        Tag t3 = new Tag(null, "Coxa", null, u1);
        tagRepository.saveAll(Arrays.asList(t1, t2, t3));

        Exercise e1 = new Exercise(null, "Supino reto", Arrays.asList(t1), u1);
        Exercise e2 = new Exercise(null, "Puxada alta", Arrays.asList(t2), u1);
        Exercise e3 = new Exercise(null, "Agachamento livre", Arrays.asList(t3), u1);
        exerciseRepository.saveAll(Arrays.asList(e1, e2, e3));

        TrainingRoutine tr1 = new TrainingRoutine(null, "Treino ABC", "Treinamento para membros superiores e inferiores.", "Hipertrofia", 3, true, u1);
        trainingRoutineRepository.save(tr1);

        u1.getUserTrainingInfo().setActiveRoutine(tr1);
        userTrainingInfoRepository.save(u1.getUserTrainingInfo());
        userRepository.save(u1);

        TrainingSession ts1 = new TrainingSession(null, 1, "A - Peitoral", tr1, true);
        TrainingSession ts2 = new TrainingSession(null, 2, "B - Dorsais", tr1, false);
        TrainingSession ts3 = new TrainingSession(null, 3, "C - Perna", tr1, false);
        trainingSessionRepository.saveAll(Arrays.asList(ts1, ts2, ts3));

        SessionExercise se1 = new SessionExercise(null, e1, 4, 12, 60., ts1);
        SessionExercise se2 = new SessionExercise(null, e2, 6, 10, 90., ts2);
        SessionExercise se3 = new SessionExercise(null, e3, 4, 20, 120., ts3);
        sessionExerciseRepository.saveAll(Arrays.asList(se1, se2, se3));

    }

    private void instantiateDietRelated(User u1) {
        Food f1 = new Food(null, "Arroz", 100., Unit.GRAM, 28., 2.5, 0., 128., u1);
        Food f2 = new Food(null, "Banana", 100., Unit.GRAM, 23.9, 1.4, 0.3, 92., u1);
        Food f3 = new Food(null, "Iogurte de Morango", 200., Unit.ML, 27., 6., 3., 160., u1);
        Food f4 = new Food(null, "Farelo de Aveia", 100., Unit.GRAM, 66., 18., 7., 250., u1);
        Food f5 = new Food(null, "Feijão", 100., Unit.GRAM, 63., 21., 0.3, 339., u1);
        Food f6 = new Food(null, "Ovo", 1., Unit.UNIT, 1., 6., 6., 80., u1);
        Food f7 = new Food(null, "Frango", 100., Unit.GRAM, 3.3, 27., 8.8, 199., u1);
        Food f8 = new Food(null, "Leite", 200., Unit.ML, 9., 6.3, 6., 115., u1);
        Food f9 = new Food(null, "Whey", 30., Unit.GRAM, 5., 23., 0., 116., u1);
        Food f10 = new Food(null, "Mel", 20., Unit.GRAM, 16., 0., 0., 64., u1);
        foodRepository.saveAll(Arrays.asList(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10));

        Recipe r1 = new Recipe(null, "Vitamina de Banana", 1.0, Unit.UNIT, null, u1);
        recipeRepository.save(r1);

        Ingredient i1 = new Ingredient(null, 100.0, f2, r1);
        Ingredient i2 = new Ingredient(null, 200.0, f8, r1);
        Ingredient i3 = new Ingredient(null, 30.0, f9, r1);
        Ingredient i4 = new Ingredient(null, 20.0, f10, r1);
        List<Ingredient> ingredients = Arrays.asList(i1, i2, i3, i4);
        ingredients = ingredientRepository.saveAll(ingredients);

        r1.setIngredients(ingredients);
        recipeRepository.save(r1);

        MacrosCount mc0 = new MacrosCount(null, 2048.5, 358.599, 127.2, 30.55, null, null, null);
        macrosCountRepository.saveAll(Arrays.asList(mc0));

        DailyMeal d1 = new DailyMeal(null, LocalDate.now(), mc0, u1);
        dailyMealRepository.save(d1);

        mc0.setDailyMeal(d1);
        macrosCountRepository.saveAll(Arrays.asList(mc0));

        mealNameRepository.deleteAllByUserId(u1.getId());
        MealName mn1 = new MealName(null, 1, "Café da manhã", u1);
        MealName mn2 = new MealName(null, 2, "Almoço", u1);
        MealName mn3 = new MealName(null, 3, "Janta", u1);
        mealNameRepository.saveAll(Arrays.asList(mn1, mn2, mn3));

        Meal m1 = new Meal(null, mn1.getName(), mn1.getOrderNumber(), d1);
        Meal m2 = new Meal(null, mn2.getName(), mn2.getOrderNumber(), d1);
        Meal m3 = new Meal(null, mn3.getName(), mn3.getOrderNumber(), d1);
        mealRepository.saveAll(Arrays.asList(m1, m2, m3));

        MacrosCount mc1 = new MacrosCount(null, 230.0, 59.75, 3.499, 0.75, null, null, null);
        MacrosCount mc2 = new MacrosCount(null, 250.0, 66.0, 18.0, 7.0, null, null, null);
        MacrosCount mc3 = new MacrosCount(null, 160.0, 27.0, 6.0, 3.0, null, null, null);
        MacrosCount mc4 = new MacrosCount(null, 384.0, 84.0, 7.5, 0.0, null, null, null);
        MacrosCount mc5 = new MacrosCount(null, 339.0, 63.0, 21.0, 0.3, null, null, null);
        MacrosCount mc6 = new MacrosCount(null, 298.5, 4.95, 40.5, 13.2, null, null, null);
        MacrosCount mc7 = new MacrosCount(null, 387.0, 53.9, 30.7, 6.3, null, null, null);
        macrosCountRepository.saveAll(Arrays.asList(mc1, mc2, mc3, mc4, mc5, mc6, mc7));

        MealEntry me1 = new MealEntry(null, 250., m1, f2, null, mc1, d1);
        MealEntry me2 = new MealEntry(null, 100., m1, f4, null, mc2, d1);
        MealEntry me3 = new MealEntry(null, 200.0, m1, f3, null, mc3, d1);

        MealEntry me4 = new MealEntry(null, 300.0, m2, f1, null, mc4, d1);
        MealEntry me5 = new MealEntry(null, 100.0, m2, f5, null, mc5, d1);
        MealEntry me6 = new MealEntry(null, 150.0, m2, f7, null, mc6, d1);

        MealEntry me7 = new MealEntry(null, 1., m3, null, r1, mc7, d1);

        mealEntryRepository.saveAll(Arrays.asList(me1, me2, me3, me4, me5, me6, me7));

        mc1.setMealEntry(me1);
        mc2.setMealEntry(me2);
        mc3.setMealEntry(me3);
        mc4.setMealEntry(me4);
        mc5.setMealEntry(me5);
        mc6.setMealEntry(me6);
        mc7.setMealEntry(me7);
        macrosCountRepository.saveAll(Arrays.asList(mc1, mc2, mc3, mc4, mc5, mc6, mc7));
//
//
//        MacrosCount mg1 = macrosCountRepository.findByUserMealInfoId(u1.getUserMealInfo().getId());
        MacrosCount mg1 = u1.getUserMealInfo().getMacrosGoal();
        mg1.setCaloriesCount(3000.);
        mg1.setCarbsCount(400.);
        mg1.setProteinsCount(160.);
        mg1.setFatsCount(80.);
        macrosCountRepository.save(mg1);
    }

    public void populateUser(User user) {
        instantiateTrainingRelated(user);
        instantiateDietRelated(user);
    }
}
