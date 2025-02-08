package com.example.test.domain;

import java.time.Instant;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.test.domain.request.job.UpdateJobDTO;
import com.example.test.domain.response.ResponseEmailJob;
import com.example.test.domain.response.resume.ResponseJobResumeDTO;
import com.example.test.utils.constant.LevelEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Location is required")
    private String location;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Level is required")
    private LevelEnum level;
    @NotNull(message = "Salary is required")
    private Double salary;
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private Instant createdAt, updatedAt, startDate, endDate;
    private String createdBy, updatedBy;

    @Column(name = "active")
    @NotNull(message = "Active is required")
    private Boolean active;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobResume> resumes = new ArrayList<>();

    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @ManyToMany(targetEntity = Skill.class, fetch = FetchType.LAZY)
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @JsonIgnoreProperties(value = { "jobs" })
    private List<Skill> skills;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinTable(name = "job_user", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties(value = { "jobs" })
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getIsActive() {
        return active;
    }

    public void setIsActive(Boolean active) {
        this.active = active;
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

    public ResponseEmailJob convertJobToResponseEmailJob(Job job) {
        ResponseEmailJob responseEmailJob = new ResponseEmailJob();
        responseEmailJob.setName(job.getName());
        responseEmailJob.setSalary(job.getSalary());
        responseEmailJob.setCompany(new ResponseEmailJob.CompanyEmail(job.getCompany().getName()));
        responseEmailJob.setId(job.getId());
        List<Skill> skills = job.getSkills();
        List<ResponseEmailJob.SkillEmail> skillEmails = skills.stream().map(skill -> {
            ResponseEmailJob.SkillEmail skillEmail = new ResponseEmailJob.SkillEmail();
            skillEmail.setName(skill.getName());
            return skillEmail;
        }).collect(Collectors.toList());
        responseEmailJob.setSkills(skillEmails);
        return responseEmailJob;
    }

    public static Job convertToJob(UpdateJobDTO job) {
        Job entity = new Job();
        entity.setQuantity(job.getQuantity());
        entity.setName(job.getName());
        entity.setDescription(job.getDescription());
        entity.setActive(job.getActive());
        entity.setLevel(job.getLevel());
        entity.setLocation(job.getLocation());
        entity.setSalary(job.getSalary());
        entity.setStartDate(job.getStartDate());
        entity.setEndDate(job.getEndDate());
        return entity;
    }

}
