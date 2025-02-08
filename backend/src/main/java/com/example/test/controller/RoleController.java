package com.example.test.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Role;
import com.example.test.domain.request.role.CreateRoleDTO;
import com.example.test.domain.request.role.UpdateRoleDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseString;
import com.example.test.service.RoleService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("")
    public ResponseEntity<CreateRoleDTO> create(@Valid @RequestBody CreateRoleDTO role) throws BadRequestException {
        Boolean isExist = this.roleService.existsByName(role.getName());

        if (isExist) {
            throw new BadRequestException("Role is already exist");
        }

        CreateRoleDTO entity = this.roleService.create(role);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateRoleDTO> update(@PathVariable String id, @RequestBody UpdateRoleDTO role)
            throws BadRequestException {
        UpdateRoleDTO entity = this.roleService.update(id, role);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponsePaginationDTO getRoles(@Filter Specification<Role> spec, Pageable pageable) {
        return this.roleService.getRoles(spec, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseString> delete(@PathVariable String id) throws BadRequestException {
        this.roleService.delete(id);
        return new ResponseEntity<>(new ResponseString("Role deleted"), HttpStatus.OK);
    }

    @GetMapping("/get-one/{id}")
    public Role getById(@PathVariable String id) throws BadRequestException {
        return this.roleService.getRoleById(id);
    }

}
