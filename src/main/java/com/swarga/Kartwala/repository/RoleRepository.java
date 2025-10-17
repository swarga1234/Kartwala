package com.swarga.Kartwala.repository;

import com.swarga.Kartwala.model.AppRole;
import com.swarga.Kartwala.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(AppRole appRole);
}
