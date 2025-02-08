package com.example.test.domain.response.resume;

import java.time.Instant;

import com.example.test.utils.constant.LevelEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ResponseJobResumeDTO {
    private Long id;

    private String name, location, status;

    private Instant updatedAt, createdAt;

    public Long getId() {
        return id;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    private ResponseCompanyResumeDTO company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseCompanyResumeDTO getCompany() {
        return company;
    }

    public void setCompany(ResponseCompanyResumeDTO company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
