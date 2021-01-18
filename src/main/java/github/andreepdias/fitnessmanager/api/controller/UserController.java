package github.andreepdias.fitnessmanager.api.controller;

import github.andreepdias.fitnessmanager.api.dto.UserDTO;
import github.andreepdias.fitnessmanager.exceptions.DuplicatedObjectException;
import github.andreepdias.fitnessmanager.mapper.UserMapper;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.service.DBService;
import github.andreepdias.fitnessmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final DBService dBservice;

    private final UserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDTO register(@RequestBody @Valid UserDTO dto){
        User user = mapper.toEntity(dto);
        User freshUser;
        try{
            freshUser = service.insert(user);
        }catch(DuplicatedObjectException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toDto(freshUser);
    }

    @PostMapping("populate")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO registerWithData(@RequestBody @Valid UserDTO dto){
        User user = mapper.toEntity(dto);
        try{
            user = service.insert(user);
            dBservice.populateUser(user);
        }catch(DuplicatedObjectException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toDto(user);
    }

}
