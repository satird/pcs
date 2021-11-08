package ru.satird.pcs.dto;

import lombok.Getter;
import lombok.Setter;
import ru.satird.pcs.domains.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserInfoDto {

    private Long id;
    private String name;
    private String email;
    private boolean active;
    private Set<Role> roles = new HashSet<>();
}
