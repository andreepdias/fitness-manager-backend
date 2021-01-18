package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.exceptions.DataInconsistencyException;
import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Training.Tag;
import github.andreepdias.fitnessmanager.repository.ExerciseRepository;
import github.andreepdias.fitnessmanager.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    public List<Tag> findAll() {
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserId(userId);
    }

    public Page<Tag> findPage(PageRequest pageRequest) {
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserId(pageRequest, userId);
    }

    public Tag findById(Integer id){
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tag with id " + id + " was not found."));
    }

    private boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public Tag create(Tag tag) {
        tag.setId(null);
        return repository.save(tag);
    }

    public void update(Tag tag) {
        Tag oldTag = findById(tag.getId());
        updateTagData(oldTag, tag);
        repository.save(oldTag);
    }

    private void updateTagData(Tag oldTag, Tag tag) {
        oldTag.setName(tag.getName());
    }

    public void deleteById(Integer id) {
        boolean existsById = existsById(id);
        if(!existsById){
            throw new ObjectNotFoundException("Tag with id " + id + " was not found.");
        }
        boolean existsExercisesAssociated = existsExercisesAssociated(id);
        if(existsExercisesAssociated){
            throw new DataInconsistencyException("You can't delete a tag with exercises associated.");
        }
        repository.deleteById(id);
    }

    private boolean existsExercisesAssociated(Integer exerciseId) {
        return exerciseRepository.existsByTags_Id(exerciseId);
    }
}
