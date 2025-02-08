package com.example.test.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Permission;
import com.example.test.domain.request.permission.UpdatePermissionDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseString;
import com.example.test.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission permission) throws BadRequestException {
        Boolean isExist = this.permissionService.existsByNameAndApiPathAndMethod(permission.getName(),
                permission.getApiPath(), permission.getMethod());

        if (isExist) {
            throw new BadRequestException("Permission is already exist");
        }

        Permission entity = this.permissionService.create(permission);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {
        return new ResponseEntity<>(this.permissionService.getPermissions(spec, pageable), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdatePermissionDTO> update(@PathVariable String id,
            @RequestBody UpdatePermissionDTO permission)
            throws BadRequestException {
        UpdatePermissionDTO entity = this.permissionService.update(id, permission);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseString> delete(@PathVariable String id) throws BadRequestException {
        this.permissionService.delete(id);
        return new ResponseEntity<>(new ResponseString("Delete success"), HttpStatus.OK);
    }

}
