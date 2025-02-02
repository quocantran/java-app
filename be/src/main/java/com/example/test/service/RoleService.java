package com.example.test.service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.test.domain.Role;
import com.example.test.domain.request.role.CreateRoleDTO;
import com.example.test.domain.request.role.UpdateRoleDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.role.ResponseRoleDTO;
import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Permission;
import java.util.List;
import java.util.stream.Collectors;

import com.example.test.repository.PermissionRepository;
import com.example.test.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository,
            ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    public Boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public CreateRoleDTO create(CreateRoleDTO role) {
        List<Permission> permissions = permissionRepository.findByIdIn(role.getPermissions());

        Role entity = new Role();

        entity.setName(role.getName());
        entity.setDescription(role.getDescription());
        entity.setPermissions(permissions);
        entity.setActive(role.getActive());

        this.roleRepository.save(entity);

        return role;

    }

    public UpdateRoleDTO update(String id, UpdateRoleDTO role) throws BadRequestException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid id");
        }

        Role entity = this.roleRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Role not found");
        }

        List<Permission> permissions = permissionRepository.findByIdIn(role.getPermissions());
        modelMapper.map(role, entity);

        entity.setPermissions(permissions.size() > 0 ? permissions : entity.getPermissions());

        this.roleRepository.save(entity);

        modelMapper.map(entity, role);

        return role;
    }

    @Cacheable(value = "roles", key = "'roles-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public ResponsePaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> roles = this.roleRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(roles.getNumber() + 1);
        meta.setPageSize(roles.getSize());

        meta.setPages(roles.getTotalPages());
        meta.setTotal(roles.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        List<ResponseRoleDTO> responseRoleDTOs = roles.stream()
                .map(role -> role.convertRoleDto())
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(responseRoleDTOs);

        return resultPaginationDTO;
    }

    public Role getRoleById(String id) throws BadRequestException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid id");
        }

        Role entity = this.roleRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Role not found");
        }

        return entity;
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public void delete(String id) throws BadRequestException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid id");
        }

        Role entity = this.roleRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Role not found");
        }

        this.roleRepository.delete(entity);
    }

}
