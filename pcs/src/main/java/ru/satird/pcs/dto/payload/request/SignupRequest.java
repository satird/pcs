package ru.satird.pcs.dto.payload.request;

import lombok.Getter;
import lombok.Setter;
import ru.satird.pcs.validation.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String username;
    @NotBlank
    @Email
    @ValidEmail
    private String email;
    private Set<String> role;
    @NotBlank
    private String password;
}
