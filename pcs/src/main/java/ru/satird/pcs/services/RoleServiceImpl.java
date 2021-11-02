package ru.satird.pcs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.ERole;
import ru.satird.pcs.domains.Role;
import ru.satird.pcs.repositories.RoleRepository;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findRoleByName(ERole roleUser) {
        return roleRepository.findByName(roleUser);
    }
}
