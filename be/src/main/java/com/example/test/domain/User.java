package com.example.test.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.test.domain.response.user.ResponseUserDTO;
import com.example.test.utils.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nimbusds.oauth2.sdk.auth.verifier.Context;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    private String avatar;

    @Nullable
    private Integer age;

    public void setFollowedCompanies(List<Company> followedCompanies) {
        this.followedCompanies = followedCompanies;
    }

    @Nullable
    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Resume> resumes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt, updatedAt;
    private String createdBy, updatedBy;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    public List<Company> getFollowedCompanies() {
        return followedCompanies;
    }

    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(targetEntity = Company.class, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "usersFollowed")
    @JoinTable(name = "companies_users_followed", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<Company> followedCompanies = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Job> jobs;

    public void setAge(Integer age) {
        this.age = age;
    }

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public ResponseUserDTO convertResponseUserDto() {
        ResponseUserDTO userDTO = new ResponseUserDTO();
        userDTO.setId(this.getId());
        userDTO.setName(this.getName());
        userDTO.setEmail(this.getEmail());
        userDTO.setAvatar(this.getAvatar());
        userDTO.setCompany(company != null ? company.convertCompanyDto() : null);
        userDTO.setRole(role != null ? role.convertRoleDto() : null);
        userDTO.setPermissions(role.getPermissions());
        userDTO.setGender(gender);
        userDTO.setAddress(address);
        userDTO.setAge(age);

        return userDTO;
    }
}
