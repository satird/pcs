package ru.satird.pcs.services;

import ru.satird.pcs.domains.ERole;
import ru.satird.pcs.domains.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findRoleByName(ERole roleUser);
}
