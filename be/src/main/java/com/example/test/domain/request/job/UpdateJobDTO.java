package com.example.test.domain.request.job;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

import com.example.test.utils.constant.LevelEnum;

public class UpdateJobDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @NotNull(message = "Salary is required")
    private Double salary;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Description is required")
    private String description;

    private Boolean active;

    private List<Long> skills;

    @NotNull(message = "Start date is required")
    private Instant startDate;

    @NotNull(message = "End date is required")
    private Instant endDate;

    public UpdateJobDTO(String name, String location, LevelEnum level, Double salary, Integer quantity,
            String description, Boolean active, List<Long> skills, Instant startDate, Instant endDate) {
        this.name = name;
        this.location = location;
        this.level = level;
        this.salary = salary;
        this.quantity = quantity;
        this.description = description;
        this.active = active;
        this.skills = skills;
        this.startDate = startDate;
        this.endDate = endDate;
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

    // Getters and setters
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Long> getSkills() {
        return skills;
    }

    public void setSkills(List<Long> skills) {
        this.skills = skills;
    }
}