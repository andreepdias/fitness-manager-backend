package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Training.Exercise;
import github.andreepdias.fitnessmanager.repository.ExerciseRepository;
import github.andreepdias.fitnessmanager.repository.SessionExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository repository;
    private final SessionExerciseRepository sessionExerciseRepository;
    private final UserService userService;

    public List<Exercise> findAll() {
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserId(userId);
    }

    public Page<Exercise> findPage(PageRequest pageRequest) {
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserId(pageRequest, userId);
    }

    public Exercise findById(Integer id){
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Exercise with id " + id + " was not found."));
    }

    private boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public Exercise create(Exercise exercise) {
        exercise.setId(null);
        return repository.save(exercise);
    }

    public void update(Exercise exercise) {
        Exercise oldExercise = findById(exercise.getId());
        updateExerciseData(oldExercise, exercise);
        repository.save(oldExercise);
    }

    private void updateExerciseData(Exercise oldExercise, Exercise exercise) {
        oldExercise.setName(exercise.getName());
        oldExercise.setTags(exercise.getTags());
    }

    public void deleteById(Integer id) {
        boolean existsById = existsById(id);
        if(!existsById){
            throw new ObjectNotFoundException("Exercise with id " + id + " was not found.");
        }
        boolean existsSessionExercisesAssociated = existsSessionExercisesAssociated(id);
        if(existsSessionExercisesAssociated){
            throw new DataInconsistencyException("You can't delete a exercise if it's part of some training routine.");
        }
        repository.deleteById(id);
    }

    private boolean existsSessionExercisesAssociated(Integer exerciseId) {
        return sessionExerciseRepository.existsByExerciseId(exerciseId);
    }
}
