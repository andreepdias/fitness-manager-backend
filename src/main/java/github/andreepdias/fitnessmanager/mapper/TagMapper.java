package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.TagDTO;
import github.andreepdias.fitnessmanager.model.entity.Training.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagMapper {

    private final ModelMapper mapper;

    public Tag toEntity(TagDTO dto){
        return mapper.map(dto, Tag.class);
    }

    public TagDTO toDTO(Tag entity){
        return mapper.map(entity, TagDTO.class);
    }
}
