package com.example.test.domain;

import java.time.Instant;

import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    private String name;

    private Instant createdAt, updatedAt;
    private String createdBy, updatedBy;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Subscriber> subscribers;

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
}
