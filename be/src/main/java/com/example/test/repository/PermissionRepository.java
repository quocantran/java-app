package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.test.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    Permission save(Permission permission);

    Permission findByName(String name);

    Permission findById(long id);

    Boolean existsById(long id);

    Boolean existsByNameAndApiPathAndMethod(String name, String apiPath, String method);

    List<Permission> findByIdIn(List<Long> permission);

}
