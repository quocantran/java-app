package com.example.test.domain.request.user;

import java.time.Instant;

import com.example.test.domain.Company;
import com.example.test.domain.Role;
import com.example.test.utils.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateUserDTO {
    private String name, address, avatar, email;

    private Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt, updatedAt;

    private Role role;
    private Company company;

    public GenderEnum getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    private GenderEnum gender;

    public UpdateUserDTO(String name, String address, String avatar, Integer age, Instant createdAt, Instant updatedAt,
            Role role, Company company) {
        this.name = name;
        this.address = address;
        this.avatar = avatar;
        this.age = age;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
