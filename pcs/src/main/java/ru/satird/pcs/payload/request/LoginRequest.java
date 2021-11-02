package ru.satird.pcs.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    @NotNull
    private String username;
    @NotBlank
    @NotNull
    private String password;
}
