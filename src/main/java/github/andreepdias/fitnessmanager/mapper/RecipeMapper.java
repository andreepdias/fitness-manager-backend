package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.RecipeDTO;
import github.andreepdias.fitnessmanager.model.entity.Diet.Recipe;
import github.andreepdias.fitnessmanager.model.entity.enums.Unit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeMapper {

    private final ModelMapper mapper;
//    private final IngredientService ingredientService;

    public Recipe toEntity(RecipeDTO dto){
        Recipe recipe = mapper.map(dto, Recipe.class);
        recipe.setUnit(Unit.toEnum(dto.getUnit()));
        return recipe;
    }

    public RecipeDTO toDTO(Recipe recipe){
        RecipeDTO dto = mapper.map(recipe, RecipeDTO.class);
        dto.setUnit(recipe.getUnit().getDescription());
        return dto;
    }
}
