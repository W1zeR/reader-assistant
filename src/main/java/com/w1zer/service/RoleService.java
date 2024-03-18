package com.w1zer.service;

import com.w1zer.entity.Role;
import com.w1zer.entity.RoleName;
import com.w1zer.exception.NotFoundException;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private static final String ROLE_WITH_NAME_NOT_FOUND = "Role with name '%s' not found";

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(RoleName name){
        return roleRepository.findByName(name).orElseThrow(
                () -> new NotFoundException(ROLE_WITH_NAME_NOT_FOUND.formatted(name))
        );
    }
}
