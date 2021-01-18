package github.andreepdias.fitnessmanager.api.controller;

import github.andreepdias.fitnessmanager.api.dto.FoodDTO;
import github.andreepdias.fitnessmanager.mapper.FoodMapper;
import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Diet.Food;
import github.andreepdias.fitnessmanager.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService service;
    private FoodMapper mapper = new FoodMapper();

    @GetMapping("all")
    public List<FoodDTO> getAll(){
        List<Food> foods = service.findAll();
        return foods.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public Page<FoodDTO> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return service.findPage(pageRequest).map(mapper::toDto);
    }

    @GetMapping("{id}")
    public FoodDTO getById(@PathVariable Integer id){
        Food food;
        try {
            food = service.findByIdAndAuthenticatedUser(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toDto(food);
    }

    @PostMapping
    public FoodDTO create(@RequestBody FoodDTO dto){
        Food food = mapper.toEntity(dto);
        food = service.create(food);
        return mapper.toDto(food);
    }

    @PostMapping("{id}")
    public void update(@PathVariable Integer id, @RequestBody FoodDTO dto){
        Food food = mapper.toEntity(dto);
        food.setId(id);

        try {
            service.update(food);
        }catch(ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        try{
            service.deleteById(id);
        }catch(ObjectNotFoundException | DataInconsistencyException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

}
