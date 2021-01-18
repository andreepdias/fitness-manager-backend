package github.andreepdias.fitnessmanager.api.controller;

import github.andreepdias.fitnessmanager.api.dto.TagDTO;
import github.andreepdias.fitnessmanager.mapper.TagMapper;
import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Training.Tag;
import github.andreepdias.fitnessmanager.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;
    private final TagMapper mapper;

    @GetMapping("{id}")
    public TagDTO get(@PathVariable Integer id){
        Tag tag;
        try{
            tag = service.findById(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toDTO(tag);
    }

    @GetMapping("all")
    public List<TagDTO> getAll(){
        List<Tag> tags = service.findAll();
        return tags.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping
    public Page<TagDTO> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Tag> tags = service.findPage(pageRequest);
        return tags.map(mapper::toDTO);
    }

    @PostMapping
    public TagDTO create(@RequestBody TagDTO dto){
        Tag tag = mapper.toEntity(dto);
        tag = service.create(tag);
        return mapper.toDTO(tag);
    }

    @PostMapping("{id}")
    public void update(@RequestBody TagDTO dto, @PathVariable Integer id){
        Tag tag = mapper.toEntity(dto);
        try{
            tag.setId(id);
            service.update(tag);
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
