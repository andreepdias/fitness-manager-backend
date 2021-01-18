package github.andreepdias.fitnessmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotEmpty(message = "User name can't be empty.")
    private String name;

    @NotEmpty(message = "User email can't be empty.")
    private String email;

    @NotEmpty(message = "User password can't be empty.")
    private String password;
}
