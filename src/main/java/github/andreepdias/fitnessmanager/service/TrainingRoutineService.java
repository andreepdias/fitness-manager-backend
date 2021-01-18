package github.andreepdias.fitnessmanager.service;

import github.andreepdias.fitnessmanager.exceptions.ObjectNotFoundException;
import github.andreepdias.fitnessmanager.model.entity.Training.SessionExercise;
import github.andreepdias.fitnessmanager.model.entity.Training.TrainingRoutine;
import github.andreepdias.fitnessmanager.model.entity.Training.TrainingSession;
import github.andreepdias.fitnessmanager.model.entity.User;
import github.andreepdias.fitnessmanager.repository.SessionExerciseRepository;
import github.andreepdias.fitnessmanager.repository.TrainingRoutineRepository;
import github.andreepdias.fitnessmanager.repository.TrainingSessionRepository;
import github.andreepdias.fitnessmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingRoutineService {

    private final TrainingRoutineRepository repository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final SessionExerciseRepository sessionExerciseRepository;

    private final UserService userService;
    private final UserRepository userRepository;

    public List<TrainingRoutine> findAll() {
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserId(userId);
    }

    public Page<TrainingRoutine> findPage(PageRequest pageRequest) {
        Integer userId = userService.findAuthenticatedUser().getId();
        return repository.findAllByUserId(pageRequest, userId);
    }

    public TrainingRoutine findById(Integer id){
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("TrainingRoutine with id " + id + " was not found."));
    }

    private boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public TrainingRoutine create(TrainingRoutine trainingRoutine) {
        User user = userService.findAuthenticatedUser();

        trainingRoutine.setUser(user);
        trainingRoutine.setId(null);

        trainingRoutine = repository.save(trainingRoutine);

        if(trainingRoutine.isActiveRoutine()){
            userService.setActiveTrainingRoutine(trainingRoutine);
        }

        createEmptyTrainingSessions(trainingRoutine);
        return trainingRoutine;
    }

    private void createEmptyTrainingSessions(TrainingRoutine trainingRoutine) {
        List<TrainingSession> toCreateTrainingSessions = new ArrayList<>();
        for(int i = 1; i <= trainingRoutine.getWeekFrequency(); i++){
            TrainingSession trainingSession = new TrainingSession(null, i, String.valueOf((char)('A' + (i - 1))), trainingRoutine, false);
            toCreateTrainingSessions.add(trainingSession);
        }
        createAllTrainingSessions(toCreateTrainingSessions);
    }

    private List<TrainingSession> createAllTrainingSessions(List<TrainingSession> trainingSessions) {
        return trainingSessionRepository.saveAll(trainingSessions);
    }

    public void update(TrainingRoutine trainingRoutine) {
        TrainingRoutine oldTrainingRoutine = findById(trainingRoutine.getId());

        updateTrainingRoutineData(oldTrainingRoutine, trainingRoutine);
        updateTrainingSessions(trainingRoutine);

        oldTrainingRoutine = repository.save(oldTrainingRoutine);

        if(oldTrainingRoutine.isActiveRoutine()){
            userService.setActiveTrainingRoutine(trainingRoutine);
        }else{
            userService.unsetActiveTrainingRoutine(trainingRoutine);
        }
    }

    private void updateTrainingSessions(TrainingRoutine trainingRoutine) {
        List<TrainingSession> sessions = findAllTrainingSessionsByTrainingRoutineId(trainingRoutine.getId());

        if(trainingRoutine.getWeekFrequency() < sessions.size()){
            deleteUnusedTrainingSessions(trainingRoutine, sessions);
        }else if(trainingRoutine.getWeekFrequency() > sessions.size()){
            createNewSessions(trainingRoutine, sessions);
        }

    }

    private void createNewSessions(TrainingRoutine trainingRoutine, List<TrainingSession> sessions) {
        List<TrainingSession> toCreateSessions = new ArrayList<>();

        for(int i = sessions.size() + 1; i <= trainingRoutine.getWeekFrequency(); i++){
            TrainingSession trainingSession = new TrainingSession(null, i, String.valueOf((char)('A' + (i - 1))), trainingRoutine, false);
            toCreateSessions.add(trainingSession);
        }
        if(toCreateSessions.size() > 0){
            toCreateSessions.get(0).setCurrentSession(true);
        }
        createAllTrainingSessions(toCreateSessions);
    }

    private void deleteUnusedTrainingSessions(TrainingRoutine trainingRoutine, List<TrainingSession> sessions) {
        List<Integer> toDeleteIds = new ArrayList<>();
        for (TrainingSession session : sessions) {
            if(session.getOrderNumber() > trainingRoutine.getWeekFrequency()){
                toDeleteIds.add(session.getId());
                deleteAllSessionExercisesByTrainingSessionId(session.getId());
            }
        }
        deleteAllTrainingSessionsById(toDeleteIds);
    }

    private void deleteAllTrainingSessionsById(List<Integer> toDeleteIds) {
        trainingSessionRepository.deleteByIdIn(toDeleteIds);
    }

    private void updateTrainingRoutineData(TrainingRoutine oldTrainingRoutine, TrainingRoutine trainingRoutine) {
        oldTrainingRoutine.setName(trainingRoutine.getName());
        oldTrainingRoutine.setDescription(trainingRoutine.getDescription());
        oldTrainingRoutine.setWeekFrequency(trainingRoutine.getWeekFrequency());
        oldTrainingRoutine.setGoal(trainingRoutine.getGoal());
        oldTrainingRoutine.setActiveRoutine(trainingRoutine.isActiveRoutine());
    }

    public void deleteById(Integer id) {
        boolean existsById = existsById(id);
        if(!existsById){
            throw new ObjectNotFoundException("TrainingRoutine with id " + id + " was not found.");
        }
        deleteTrainingSessionsByTrainingRoutineId(id);
        unsetIfActiveUserTrainingRoutine(id);
        repository.deleteById(id);
    }

    private void unsetIfActiveUserTrainingRoutine(Integer trainingRoutineId) {
        User user = userService.findAuthenticatedUser();
        if(user.getUserTrainingInfo().getActiveRoutine().getId() == trainingRoutineId){
            user.getUserTrainingInfo().setActiveRoutine(null);
            userRepository.save(user);
        }
    }

    private void deleteTrainingSessionsByTrainingRoutineId(Integer trainingRoutineId) {
        deleteSessionExercises(trainingRoutineId);
        trainingSessionRepository.deleteAllByTrainingRoutineId(trainingRoutineId);
    }

    private void deleteSessionExercises(Integer trainingRoutineId) {
        List<TrainingSession> trainingSessions = findAllTrainingSessionsByTrainingRoutineId(trainingRoutineId);
        for (TrainingSession trainingSession : trainingSessions) {
            deleteAllSessionExercisesByTrainingSessionId(trainingSession.getId());
        }
    }

    private void deleteAllSessionExercisesByTrainingSessionId(Integer trainingSessionId) {
        sessionExerciseRepository.deleteAllByTrainingSessionId(trainingSessionId);
    }

    public List<TrainingSession> findAllTrainingSessionsByTrainingRoutineId(Integer trainingRoutineId) {
        return trainingSessionRepository.findAllByTrainingRoutineIdOrderByOrderNumber(trainingRoutineId);
    }

    public List<SessionExercise> findSessionExercisesByTrainingSessionId(Integer trainingSessionId) {
        return sessionExerciseRepository.findAllByTrainingSessionId(trainingSessionId);
    }

    public TrainingRoutine findActiveTrainingRoutine() {
        User user = userService.findAuthenticatedUser();
        return user.getUserTrainingInfo().getActiveRoutine();
    }

    public void deleteSessionExerciseById(Integer id) {
        boolean existsById = existsSessionExerciseById(id);
        if(!existsById){
            throw new ObjectNotFoundException("Session exercise with id " + id + " was not found.");
        }
        sessionExerciseRepository.deleteById(id);
    }

    private boolean existsSessionExerciseById(Integer id) {
        return sessionExerciseRepository.existsById(id);
    }

    public TrainingSession findTrainingSessionById(Integer trainingSessionId) {
        return trainingSessionRepository.findById(trainingSessionId)
                .orElseThrow(() -> new ObjectNotFoundException("Training Session with id " + trainingSessionId + " was not found."));
    }

    public SessionExercise createSessionExercise(SessionExercise sessionExercise) {
        sessionExercise.setId(null);
        return  sessionExerciseRepository.save(sessionExercise);
    }

    public void updateSessionExercise(SessionExercise sessionExercise) {
        SessionExercise oldSessionExercise = findSessionExerciseById(sessionExercise.getId());
        updateSessionExerciseData(oldSessionExercise, sessionExercise);
        sessionExerciseRepository.save(oldSessionExercise);
    }

    private void updateSessionExerciseData(SessionExercise oldSessionExercise, SessionExercise sessionExercise) {
        oldSessionExercise.setSets(sessionExercise.getSets());
        oldSessionExercise.setRepetitions(sessionExercise.getRepetitions());
        oldSessionExercise.setRestInterval(sessionExercise.getRestInterval());
        oldSessionExercise.setExercise(sessionExercise.getExercise());
    }

    private SessionExercise findSessionExerciseById(Integer id) {
        return sessionExerciseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Session exercise with id " + id + " was not found."));
    }

    public void updateTrainingSession(TrainingSession trainingSession) {
        TrainingSession oldTrainingSession = findTrainingSessionById(trainingSession.getId());
        updateTrainingSessionData(oldTrainingSession, trainingSession);
        trainingSessionRepository.save(oldTrainingSession);
    }

    private void updateTrainingSessionData(TrainingSession oldTrainingSession, TrainingSession trainingSession) {
        oldTrainingSession.setDescription(trainingSession.getDescription());
        oldTrainingSession.setOrderNumber(trainingSession.getOrderNumber());
    }

    public TrainingSession updateCurrentTrainingSession(TrainingSession currentTrainingSession, Integer trainingRoutineId) {
        List<TrainingSession> trainingSessions = findAllTrainingSessionsByTrainingRoutineId(trainingRoutineId);

        if(trainingSessions.size() == 0){
            throw  new ObjectNotFoundException("The training routine with id " + trainingRoutineId + " from the current training session of id " + currentTrainingSession.getId() + " was not found.");
        }

        List<TrainingSession> toUpdateTrainingSessions = new ArrayList<>();

        for (int i = 0; i < trainingSessions.size(); i++) {
            if(trainingSessions.get(i).getId() == currentTrainingSession.getId()){
                trainingSessions.get(i).setCurrentSession(false);

                int nextIndex = (i + 1) % trainingSessions.size();
                trainingSessions.get(nextIndex).setCurrentSession(true);

                toUpdateTrainingSessions.add(trainingSessions.get(i));
                toUpdateTrainingSessions.add(trainingSessions.get(nextIndex));

                break;
            }
        }

        if(toUpdateTrainingSessions.size() == 0){
            throw new ObjectNotFoundException("Current training session with id " + currentTrainingSession.getId() + " does not exists in its training routine of id " + trainingRoutineId + ".");
        }

        toUpdateTrainingSessions = updateAllTrainingSessions(toUpdateTrainingSessions);
        return toUpdateTrainingSessions.get(1);
    }

    private List<TrainingSession> updateAllTrainingSessions(List<TrainingSession> toUpdateTrainingSessions) {
        return trainingSessionRepository.saveAll(toUpdateTrainingSessions);
    }
}
