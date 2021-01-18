package github.andreepdias.fitnessmanager.api.controller;

import github.andreepdias.fitnessmanager.api.dto.IngredientDTO;
import github.andreepdias.fitnessmanager.api.dto.RecipeDTO;
import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.mapper.IngredientMapper;
import github.andreepdias.fitnessmanager.mapper.RecipeMapper;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Diet.Ingredient;
import github.andreepdias.fitnessmanager.model.entity.Diet.Recipe;
import github.andreepdias.fitnessmanager.service.IngredientService;
import github.andreepdias.fitnessmanager.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService service;
    private final IngredientService ingredientService;

    private final RecipeMapper mapper;
    private final IngredientMapper ingredientMapper;

    @GetMapping("all")
    public List<RecipeDTO> getAll(){
        List<Recipe> recipes = service.findAll();
        List<RecipeDTO> recipeDTOS = recipes.stream().map(mapper::toDTO).collect(Collectors.toList());

        for (RecipeDTO recipe : recipeDTOS) {
            List<Ingredient> ingredients = ingredientService.findAllByRecipeId(recipe.getId());
            List<IngredientDTO> ingredientDTOS = ingredients.stream().map(ingredientMapper::toDTO).collect(Collectors.toList());
            recipe.setIngredients(ingredientDTOS);
        }
        return recipeDTOS;
    }

    @GetMapping
    public Page<RecipeDTO> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Recipe> recipes = service.findPage(pageRequest);
        Page<RecipeDTO> recipeDTOS = recipes.map(mapper::toDTO);

        for (RecipeDTO recipe : recipeDTOS) {
            List<Ingredient> ingredients = ingredientService.findAllByRecipeId(recipe.getId());
            List<IngredientDTO> ingredientDTOS = ingredients.stream().map(ingredientMapper::toDTO).collect(Collectors.toList());
            recipe.setIngredients(ingredientDTOS);
        }
        return recipeDTOS;
    }

    @GetMapping("{id}")
    public RecipeDTO getById(@PathVariable Integer id){
        Recipe recipe;
        try {
            recipe = service.findById(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        RecipeDTO recipeDTO = mapper.toDTO(recipe);

        List<Ingredient> ingredients = ingredientService.findAllByRecipeId(recipe.getId());
        List<IngredientDTO> ingredientDTOS = ingredients.stream().map(ingredientMapper::toDTO).collect(Collectors.toList());
        recipeDTO.setIngredients(ingredientDTOS);

        return recipeDTO;
    }

    @PostMapping
    public RecipeDTO create(@RequestBody RecipeDTO dto){
        Recipe recipe = mapper.toEntity(dto);
        recipe = service.create(recipe);
        RecipeDTO recipeDTO = mapper.toDTO(recipe);
        return recipeDTO;
    }

    @PostMapping("{id}")
    public void update(@RequestBody RecipeDTO dto, @PathVariable Integer id){
        Recipe recipe = mapper.toEntity(dto);
        recipe.setId(id);
        try{
            recipe = service.update(recipe);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        List<Ingredient> newIngredients = dto.getIngredients().stream().map(ingredientMapper::toEntity).collect(Collectors.toList());
        ingredientService.updateIngredients(newIngredients, recipe);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        try{
            service.deleteById(id);
        }catch (ObjectNotFoundException | DataInconsistencyException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

}
