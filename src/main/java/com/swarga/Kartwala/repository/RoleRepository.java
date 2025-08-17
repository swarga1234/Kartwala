package com.swarga.Kartwala.repository;

import com.swarga.Kartwala.model.AppRole;
import com.swarga.Kartwala.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(AppRole appRole);
}
