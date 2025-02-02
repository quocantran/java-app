package com.example.test.domain.response.job;

import java.time.Instant;
import java.util.List;

import com.example.test.domain.Skill;
import com.example.test.domain.response.resume.ResponseCompanyResumeDTO;
import com.example.test.domain.response.user.ResponseUserDTO;
import com.example.test.utils.constant.LevelEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ResponseJobDTO {
    private Long id;
    private String name;

    private String location;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    private Double salary;

    private Integer quantity;

    private String description;

    private Instant createdAt, updatedAt, startDate, endDate;

    private ResponseCompanyResumeDTO company;

    private List<Skill> skills;

    private Boolean active;

    private List<ResponseUserDTO> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public ResponseCompanyResumeDTO getCompany() {
        return company;
    }

    public void setCompany(ResponseCompanyResumeDTO company) {
        this.company = company;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<ResponseUserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<ResponseUserDTO> users) {
        this.users = users;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
