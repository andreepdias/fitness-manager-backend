package github.andreepdias.fitnessmanager.api.controller;

import github.andreepdias.fitnessmanager.api.dto.SessionExerciseDTO;
import github.andreepdias.fitnessmanager.api.dto.TrainingRoutineDTO;
import github.andreepdias.fitnessmanager.api.dto.TrainingSessionDTO;
import github.andreepdias.fitnessmanager.mapper.TrainingRoutineMapper;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Training.SessionExercise;
import github.andreepdias.fitnessmanager.model.entity.Training.TrainingRoutine;
import github.andreepdias.fitnessmanager.model.entity.Training.TrainingSession;
import github.andreepdias.fitnessmanager.service.TrainingRoutineService;
import github.andreepdias.fitnessmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingRoutineService service;
    private final UserService userService;

    private final TrainingRoutineMapper mapper;

    @GetMapping("active")
    public TrainingRoutineDTO getActive(){
        TrainingRoutine trainingRoutine = service.findActiveTrainingRoutine();

        TrainingRoutineDTO dto;
        if(trainingRoutine != null){
            dto = mapper.toDTO(trainingRoutine);
        }else{
            dto = new TrainingRoutineDTO(null, "", "", "", 0, false, null, null);
        }
        return dto;
    }

    @GetMapping("{id}")
    public TrainingRoutineDTO get(@PathVariable Integer id){
        TrainingRoutine trainingRoutine;
        try{
            trainingRoutine = service.findById(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return mapper.toDTO(trainingRoutine);
    }

    @GetMapping("all")
    public List<TrainingRoutineDTO> getAll(){
        List<TrainingRoutine> trainingRoutines = service.findAll();
        return trainingRoutines.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping
    public Page<TrainingRoutineDTO> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TrainingRoutine> trainingRoutines = service.findPage(pageRequest);
        return trainingRoutines.map(mapper::toDTO);
    }

    @PostMapping
    public TrainingRoutineDTO create(@RequestBody TrainingRoutineDTO dto){
        TrainingRoutine trainingRoutine = mapper.toEntity(dto);
        trainingRoutine = service.create(trainingRoutine);
        return mapper.toDTO(trainingRoutine);
    }

    @PostMapping("{id}")
    public void update(@RequestBody TrainingRoutineDTO dto, @PathVariable Integer id){
        TrainingRoutine trainingRoutine = mapper.toEntity(dto);
        try{
            trainingRoutine.setId(id);
            service.update(trainingRoutine);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        try{
            service.deleteById(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("session-exercise")
    public SessionExerciseDTO createSessionExercise(@RequestBody SessionExerciseDTO dto){
        SessionExercise sessionExercise;
        try{
            sessionExercise = mapper.toSessionExerciseEntity(dto);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        sessionExercise = service.createSessionExercise(sessionExercise);
        return mapper.toSessionExerciseDTO(sessionExercise);
    }

    @PostMapping("session-exercise/{id}")
    public void updateSessionExercise(@RequestBody SessionExerciseDTO dto, @PathVariable Integer id){
        SessionExercise sessionExercise;
        try{
            sessionExercise = mapper.toSessionExerciseEntity(dto);
            sessionExercise.setId(id);
            service.updateSessionExercise(sessionExercise);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("session-exercise/{id}")
    public void deleteSessionExercise(@PathVariable Integer id){
        try{
            service.deleteSessionExerciseById(id);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("session/{id}")
    public void updateSession(@RequestBody TrainingSessionDTO dto, @PathVariable Integer id){
        TrainingSession trainingSession;
        try{
            trainingSession = mapper.toTrainingSessionEntity(dto);
            trainingSession.setId(id);
            service.updateTrainingSession(trainingSession);
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("session/next")
    public TrainingSessionDTO updateCurrentTrainingSession(@RequestBody TrainingSessionDTO dto){
        TrainingSession currentTrainingSession = mapper.toTrainingSessionEntity(dto);

        try{
            currentTrainingSession =  service.updateCurrentTrainingSession(currentTrainingSession, dto.getTrainingRoutineId());
        }catch (ObjectNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        TrainingSessionDTO responseDTO = mapper.toTrainingSessionDTO(currentTrainingSession);
        responseDTO.setTrainingRoutineId(dto.getTrainingRoutineId());

        return responseDTO;
    }



}
