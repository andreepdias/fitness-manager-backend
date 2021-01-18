package github.andreepdias.fitnessmanager.api.controller;

import github.andreepdias.fitnessmanager.api.dto.DailyMealDTO;
import github.andreepdias.fitnessmanager.api.dto.MacrosCountDTO;
import github.andreepdias.fitnessmanager.api.dto.MealEntryDTO;
import github.andreepdias.fitnessmanager.api.dto.UserMealInfoDTO;
import github.andreepdias.fitnessmanager.mapper.DailyMealMapper;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Diet.*;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.service.MealService;
import github.andreepdias.fitnessmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService service;
    private final UserService userService;

    private final DailyMealMapper mapper;

    @GetMapping
    public DailyMealDTO getByDate(
            @RequestParam Integer day,
            @RequestParam Integer month,
            @RequestParam Integer year
    ){
        DailyMeal dailyMeal = service.findByDate(day, month, year);
        return mapper.toDTO(dailyMeal);
    }

    @PostMapping
    public DailyMealDTO createDailyMeal(
            @RequestParam Integer day,
            @RequestParam Integer month,
            @RequestParam Integer year
    ){
        User user = userService.findAuthenticatedUser();

        DailyMeal dailyMeal = service.createDailyMealByDate(day, month, year, user);
        List<Meal> meals = service.createMeals(dailyMeal, user);

        DailyMealDTO dto = mapper.newDailyMealToDTO(dailyMeal, meals);
        return dto;
    }

    @PostMapping("entries")
    public MealEntryDTO createEntry(@RequestBody MealEntryDTO dto){
        MealEntry entry;
        try {
            entry = mapper.toEntryEntity(dto);
        }catch (ObjectNotFoundException ex){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        entry = service.createEntry(entry);

        return mapper.toEntryDTO(entry);
    }

    @PostMapping("entries/{id}")
    public void updateEntry(@PathVariable Integer id, @RequestBody MealEntryDTO dto){
        MealEntry entry;
        try {
            entry = mapper.toEntryEntity(dto);
        }catch (ObjectNotFoundException ex){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        entry.setId(id);
        entry = service.updateEntry(entry);
        return;
    }

    @DeleteMapping("entries/{id}")
    public void deleteEntry(@PathVariable Integer id){
        try {
            service.deleteEntryById(id);
        }catch (ObjectNotFoundException ex){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("meals-info/{id}")
    public void updateUserMealInfo(@PathVariable Integer id, @RequestBody UserMealInfoDTO dto){
        UserMealInfo mealInfo;
        List<MealName> mealNames;
        try {
            mealInfo = mapper.toMealInfoEntity(dto);
            mealNames = mapper.toMealNameEntity(dto.getMealsName());
        }catch (ObjectNotFoundException ex){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        mealInfo.setId(id);

        service.updateMealInfo(mealInfo);
        service.updateMealNames(mealNames);
    }

    @PostMapping("meals-info/{id}/date")
    public void updateUserMealInfoWithDate(
            @PathVariable Integer id,
            @RequestBody UserMealInfoDTO dto,
            @RequestParam Integer day,
            @RequestParam Integer month,
            @RequestParam Integer year){
        UserMealInfo mealInfo;
        List<MealName> mealNames;
        try {
            mealInfo = mapper.toMealInfoEntity(dto);
            mealNames = mapper.toMealNameEntity(dto.getMealsName());
        }catch (ObjectNotFoundException ex){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        mealInfo.setId(id);

        service.updateMealInfo(mealInfo);
        List<MealName> newMealNames = service.updateMealNames(mealNames);

        service.updateDailyMealNames(day, month, year, newMealNames);
    }

    @GetMapping("meals-info/{id}")
    public UserMealInfoDTO getUserMealInfoById(@PathVariable Integer id){
        UserMealInfo userMealInfo;
        try{
            userMealInfo = service.findMealInfoById(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toMealInfoDTO(userMealInfo);
    }

    @GetMapping("meals-info")
    public UserMealInfoDTO getActiveUserMealInfo(){
        UserMealInfo userMealInfo;
        try{
            userMealInfo = service.findActiveUserMealInfo();
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toMealInfoDTO(userMealInfo);
    }

    @PostMapping("meals-info/macros/{id}")
    public void updateMacrosCount(@RequestBody MacrosCountDTO dto, @PathVariable Integer id){
        MacrosCount macrosCount = mapper.toMacrosCountEntity(dto);
        service.updateMacrosCount(macrosCount);
    }
}
