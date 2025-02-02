package com.example.test.service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Permission;
import com.example.test.domain.request.permission.UpdatePermissionDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public PermissionService(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Boolean existsByNameAndApiPathAndMethod(String name, String apiPath, String method) {
        return permissionRepository.existsByNameAndApiPathAndMethod(name, apiPath, method);
    }

    @Cacheable(value = "permissions", key = "'permissions-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public ResponsePaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissions = this.permissionRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(permissions.getNumber() + 1);
        meta.setPageSize(permissions.getSize());

        meta.setPages(permissions.getTotalPages());
        meta.setTotal(permissions.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(permissions.getContent());

        return resultPaginationDTO;
    }

    public UpdatePermissionDTO update(String id, UpdatePermissionDTO permission) throws BadRequestException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid id");
        }

        Permission entity = this.permissionRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Permission not found");
        }

        modelMapper.map(permission, entity);

        this.permissionRepository.save(entity);

        modelMapper.map(entity, permission);

        return permission;

    }

    public void delete(String id) throws BadRequestException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid id");
        }

        Permission entity = this.permissionRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Permission not found");
        }

        this.permissionRepository.delete(entity);
    }
}
