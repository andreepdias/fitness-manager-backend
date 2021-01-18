package github.andreepdias.fitnessmanager.api.controller;

import github.andreepdias.fitnessmanager.api.dto.ExerciseDTO;
import github.andreepdias.fitnessmanager.mapper.ExerciseMapper;
import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Training.Exercise;
import github.andreepdias.fitnessmanager.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService service;
    private final ExerciseMapper mapper;

    @GetMapping("{id}")
    public ExerciseDTO get(@PathVariable Integer id){
        Exercise exercise;
        try{
            exercise = service.findById(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toDTO(exercise);
    }

    @GetMapping("all")
    public List<ExerciseDTO> getAll(){
        List<Exercise> exercises = service.findAll();
        return exercises.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping
    public Page<ExerciseDTO> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Exercise> exercises = service.findPage(pageRequest);
        return exercises.map(mapper::toDTO);
    }

    @PostMapping
    public ExerciseDTO create(@RequestBody ExerciseDTO dto){
        Exercise exercise = mapper.toEntity(dto);
        exercise = service.create(exercise);
        return mapper.toDTO(exercise);
    }

    @PostMapping("{id}")
    public void update(@RequestBody ExerciseDTO dto, @PathVariable Integer id){
        Exercise exercise = mapper.toEntity(dto);
        try{
            exercise.setId(id);
            service.update(exercise);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
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
