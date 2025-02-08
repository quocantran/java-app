package com.example.test.domain.response.role;

import java.time.Instant;

public class ResponseRoleDTO {
    private Long id;
    private String name, description;
    private boolean active;

    private Instant createdAt, updatedAt;

    public ResponseRoleDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponseRoleDTO(String name, String description, boolean active, Instant createdAt, Instant updatedAt) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
