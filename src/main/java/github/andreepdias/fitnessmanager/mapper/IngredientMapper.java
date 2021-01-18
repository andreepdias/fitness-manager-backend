package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.IngredientDTO;
import github.andreepdias.fitnessmanager.model.entity.Diet.Ingredient;
import github.andreepdias.fitnessmanager.model.entity.enums.Unit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientMapper {

    private final ModelMapper mapper;
//    private final IngredientService ingredientService;

    public Ingredient toEntity(IngredientDTO dto){
        Ingredient ingredient = mapper.map(dto, Ingredient.class);
        ingredient.getFood().setUnit(Unit.toEnum(dto.getFood().getUnit()));
        return ingredient;
    }

    public IngredientDTO toDTO(Ingredient ingredient){
        IngredientDTO dto = mapper.map(ingredient, IngredientDTO.class);
        dto.getFood().setUnit(ingredient.getFood().getUnit().getDescription());
        return dto;
    }
}
