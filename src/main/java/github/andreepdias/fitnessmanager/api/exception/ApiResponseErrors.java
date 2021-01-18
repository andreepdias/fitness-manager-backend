package github.andreepdias.fitnessmanager.api.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class ApiResponseErrors {

    private List<String> errors;

    public ApiResponseErrors(String message){
        this.errors = Arrays.asList(message);
    }

    public ApiResponseErrors(List<String> errors){
        this.errors = errors;
    }
}
