package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.exceptions.DuplicatedObjectException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Diet.MacrosCount;
import github.andreepdias.fitnessmanager.model.entity.Diet.MealName;
import github.andreepdias.fitnessmanager.model.entity.Diet.UserMealInfo;
import github.andreepdias.fitnessmanager.model.entity.Training.TrainingRoutine;
import github.andreepdias.fitnessmanager.model.entity.Training.UserTrainingInfo;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserMealInfoRepository userMealInfoRepository;
    private final UserTrainingInfoRepository userTrainingInfoRepository;
    private final MacrosCountRepository macrosCountRepository;
    private final MealNameRepository mealNameRepository;
    private final DBService dbService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    private User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email " + email + " was not found."));
    }

    private boolean existsByEmail(String email){
        return repository.existsByEmail(email);
    }

    public User insert(User user) {
        if(existsByEmail(user.getEmail())){
            throw new DuplicatedObjectException("User with email " + user.getEmail() + " was already registered.");
        }
        user.setId(null);
        user = repository.save(user);

        UserMealInfo userMealInfo = new UserMealInfo(null, 3, null, user);
        userMealInfo = userMealInfoRepository.save(userMealInfo);

        MacrosCount macrosCount = new MacrosCount(null, 2000.0, 150.0, 100.0, 50.0, null, userMealInfo, null);
        macrosCount = macrosCountRepository.save(macrosCount);

        UserTrainingInfo userTrainingInfo = new UserTrainingInfo(null, null, user);
        userTrainingInfo = userTrainingInfoRepository.save(userTrainingInfo);

        user.setUserMealInfo(userMealInfo);
        user.setUserTrainingInfo(userTrainingInfo);
        user = repository.save(user);

        userMealInfo.setMacrosGoal(macrosCount);
        macrosCountRepository.save(macrosCount);

        MealName mealName1 = new MealName(null, 1, "Meal 1", user);
        MealName mealName2 = new MealName(null, 2, "Meal 2", user);
        MealName mealName3 = new MealName(null, 3, "Meal 3", user);
        mealNameRepository.saveAll(Arrays.asList(mealName1, mealName2, mealName3));


        return user;
    }

    public User findAuthenticatedUser(){
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserByEmail(email);
    }

    public void setActiveTrainingRoutine(TrainingRoutine trainingRoutine) {
        User user = findAuthenticatedUser();
        if(user.getUserTrainingInfo().getActiveRoutine() == null){
            user.getUserTrainingInfo().setActiveRoutine(trainingRoutine);
        }else if(user.getUserTrainingInfo().getActiveRoutine().getId() != trainingRoutine.getId()){
         user.getUserTrainingInfo().getActiveRoutine().setActiveRoutine(false);
         user.getUserTrainingInfo().setActiveRoutine(trainingRoutine);
        }
        repository.save(user);
    }

    public void unsetActiveTrainingRoutine(TrainingRoutine trainingRoutine) {
        User user = findAuthenticatedUser();
        if(user.getUserTrainingInfo() != null && user.getUserTrainingInfo().getActiveRoutine().getId() == trainingRoutine.getId()){
            user.getUserTrainingInfo().setActiveRoutine(null);
        }
        repository.save(user);
    }
}
