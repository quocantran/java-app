package com.example.test.domain.response.user;

import java.util.List;

import com.example.test.domain.Permission;
import com.example.test.utils.constant.GenderEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.example.test.domain.Company.ResponseCompanyDTO;
import com.example.test.domain.response.role.ResponseRoleDTO;

public class ResponseUserDTO {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    private String address;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private ResponseCompanyDTO company;
    private ResponseRoleDTO role;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    private List<Permission> permissions;

    public String getName() {
        return name;
    }

    public ResponseUserDTO(String name, String email, String avatar) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }

    public ResponseRoleDTO getRole() {
        return role;
    }

    public void setRole(ResponseRoleDTO role) {
        this.role = role;
    }

    public ResponseUserDTO() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ResponseCompanyDTO getCompany() {
        return company;
    }

    public void setCompany(ResponseCompanyDTO company) {
        this.company = company;
    }

}
