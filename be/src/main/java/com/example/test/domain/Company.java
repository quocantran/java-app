package com.example.test.domain;

import java.time.Instant;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.test.domain.response.company.ResponseCompanyUserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    private String address, logo;
    @Column(columnDefinition = "MEDIUMTEXT")
    @NotBlank(message = "Description is required")
    private String description;

    private Instant createdAt, updatedAt;
    private String createdBy, updatedBy;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "companies")
    @JoinTable(name = "companies_resumes", joinColumns = @JoinColumn(name = "company_id"), inverseJoinColumns = @JoinColumn(name = "resume_id"))
    private List<Resume> resumes;

    @ManyToMany(targetEntity = User.class)
    @JsonIgnoreProperties(value = "followedCompanies")
    @JoinTable(name = "companies_users_followed", joinColumns = @JoinColumn(name = "company_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersFollowed = new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
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

    public Long getId() {
        return id;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PreRemove
    private void preRemove() {
        for (User user : users) {
            user.setCompany(null);
        }
    }

    public static class ResponseCompanyDTO {
        private long id;
        private String name, address, logo, description;

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

    }

    public ResponseCompanyDTO convertCompanyDto() {
        ResponseCompanyDTO responseCompanyDTO = new ResponseCompanyDTO();
        responseCompanyDTO.setId(this.id);
        responseCompanyDTO.setName(this.name);
        return responseCompanyDTO;
    }

    public ResponseCompanyUserDTO convertResponseCompanyUserDTO() {
        ResponseCompanyUserDTO responseCompanyDTO = new ResponseCompanyUserDTO();
        responseCompanyDTO.setId(this.id);
        responseCompanyDTO.setName(this.name);
        responseCompanyDTO.setAddress(this.address);
        responseCompanyDTO.setLogo(this.logo);
        responseCompanyDTO.setDescription(this.description);

        List<ResponseCompanyUserDTO.UserFollowed> usersFollowed = new ArrayList<>();

        for (User user : this.usersFollowed) {
            ResponseCompanyUserDTO.UserFollowed userFollowed = new ResponseCompanyUserDTO.UserFollowed();
            userFollowed.setId(user.getId());
            userFollowed.setName(user.getName());
            usersFollowed.add(userFollowed);
        }

        responseCompanyDTO.setUsersFollowed(usersFollowed);

        return responseCompanyDTO;
    }

}
