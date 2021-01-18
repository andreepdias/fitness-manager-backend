package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.UserDTO;
import github.andreepdias.fitnessmanager.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private ModelMapper mapper = new ModelMapper();

    public User toEntity(UserDTO dto){
        return mapper.map(dto, User.class);
    }

    public UserDTO toDto(User user){
        return mapper.map(user, UserDTO.class);
    }

}
