package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.ExerciseDTO;
import github.andreepdias.fitnessmanager.model.entity.Training.Exercise;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseMapper {

    private final ModelMapper mapper;

    public Exercise toEntity(ExerciseDTO dto){
        return mapper.map(dto, Exercise.class);
    }

    public ExerciseDTO toDTO(Exercise entity){
        return mapper.map(entity, ExerciseDTO.class);
    }
}
