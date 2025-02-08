package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.test.domain.Role;
import com.example.test.domain.Skill;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Role save(Role role);

    Role findById(long id);

    Role findByName(String name);

    Boolean existsById(long id);

    Boolean existsByName(String name);

}
