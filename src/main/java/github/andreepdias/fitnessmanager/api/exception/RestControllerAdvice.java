package github.andreepdias.fitnessmanager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity handleResponseStatusException(ResponseStatusException ex){
        String message = ex.getReason();
        HttpStatus status = ex.getStatus();
        ApiResponseErrors responseErrors = new ApiResponseErrors(message);
        return new ResponseEntity(responseErrors, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationErrors(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        List<String> messages = bindingResult.getAllErrors()
                                    .stream()
                                    .map(objectError -> objectError.getDefaultMessage())
                                    .collect(Collectors.toList());
        ApiResponseErrors responseErrors = new ApiResponseErrors(messages);
        return new ResponseEntity(responseErrors, HttpStatus.BAD_REQUEST);
    }
}
