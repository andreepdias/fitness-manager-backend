package github.andreepdias.fitnessmanager.mapper;

import github.andreepdias.fitnessmanager.api.dto.SessionExerciseDTO;
import github.andreepdias.fitnessmanager.api.dto.TrainingRoutineDTO;
import github.andreepdias.fitnessmanager.api.dto.TrainingSessionDTO;
import github.andreepdias.fitnessmanager.model.entity.Training.SessionExercise;
import github.andreepdias.fitnessmanager.model.entity.Training.TrainingRoutine;
import github.andreepdias.fitnessmanager.model.entity.Training.TrainingSession;
import github.andreepdias.fitnessmanager.service.TrainingRoutineService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrainingRoutineMapper {

    private final ModelMapper mapper;
    private final TrainingRoutineService service;

    public TrainingRoutine toEntity(TrainingRoutineDTO dto){
        TrainingRoutine entity = mapper.map(dto, TrainingRoutine.class);
        entity.setActiveRoutine(dto.getIsActiveRoutine());
        return entity;
    }

    public TrainingRoutineDTO toDTO(TrainingRoutine entity){
        TrainingRoutineDTO routineDTO = mapper.map(entity, TrainingRoutineDTO.class);

        List<TrainingSessionDTO> trainingSessionDTOS = populateTrainingSessions(entity, routineDTO);
        populateSessionExercises(trainingSessionDTOS);

        return routineDTO;
    }

    private List<TrainingSessionDTO> populateTrainingSessions(TrainingRoutine entity, TrainingRoutineDTO routineDTO) {
        List<TrainingSession> trainingSessions = service.findAllTrainingSessionsByTrainingRoutineId(entity.getId());

        List<TrainingSessionDTO> trainingSessionDTOS = trainingSessions.stream().map(x -> mapper.map(x, TrainingSessionDTO.class)).collect(Collectors.toList());
        trainingSessionDTOS.forEach(x -> x.setTrainingRoutineId(routineDTO.getId()));
        routineDTO.setTrainingSessions(trainingSessionDTOS);

        return trainingSessionDTOS;
    }

    private void populateSessionExercises(List<TrainingSessionDTO> trainingSessionDTOS) {
        for (TrainingSessionDTO trainingSessionDTO : trainingSessionDTOS) {
            List<SessionExercise> sessionExercises = service.findSessionExercisesByTrainingSessionId(trainingSessionDTO.getId());
            List<SessionExerciseDTO> sessionExerciseDTOS = sessionExercises.stream().map(x -> mapper.map(x, SessionExerciseDTO.class)).collect(Collectors.toList());

            sessionExerciseDTOS.forEach(x -> x.setTrainingSessionId(trainingSessionDTO.getId()));

            trainingSessionDTO.setSessionExercises(sessionExerciseDTOS);
        }
    }

    public SessionExercise toSessionExerciseEntity(SessionExerciseDTO dto) {
        SessionExercise sessionExercise = mapper.map(dto, SessionExercise.class);

        TrainingSession trainingSession = service.findTrainingSessionById(dto.getTrainingSessionId());
        sessionExercise.setTrainingSession(trainingSession);

        return sessionExercise;
    }

    public SessionExerciseDTO toSessionExerciseDTO(SessionExercise sessionExercise) {
        SessionExerciseDTO sessionExerciseDTO = mapper.map(sessionExercise, SessionExerciseDTO.class);
        sessionExerciseDTO.setTrainingSessionId(sessionExercise.getTrainingSession().getId());
        return sessionExerciseDTO;
    }

    public TrainingSession toTrainingSessionEntity(TrainingSessionDTO dto) {
        return mapper.map(dto, TrainingSession.class);
    }

    public TrainingSessionDTO toTrainingSessionDTO(TrainingSession currentTrainingSession) {
        return mapper.map(currentTrainingSession, TrainingSessionDTO.class);
    }
}
