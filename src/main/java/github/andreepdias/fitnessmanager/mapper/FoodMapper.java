package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.FoodDTO;
import github.andreepdias.fitnessmanager.model.entity.Diet.Food;
import github.andreepdias.fitnessmanager.model.entity.enums.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    private ModelMapper mapper = new ModelMapper();

    public Food toEntity(FoodDTO dto){
        Food entity = mapper.map(dto, Food.class);
        entity.setUnit(Unit.toEnum(dto.getUnit()));
        return entity;
    }

    public FoodDTO toDto(Food food){
        FoodDTO dto = mapper.map(food, FoodDTO.class);
        dto.setUnit(food.getUnit().getDescription());
        return dto;
    }

}
