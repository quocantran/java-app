package com.example.test.domain;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.test.domain.Company.ResponseCompanyDTO;
import com.example.test.domain.response.resume.ResponseCompanyResumeDTO;
import com.example.test.domain.response.resume.ResponseJobResumeDTO;
import com.example.test.domain.response.resume.ResponseResumeDTO;
import com.example.test.utils.constant.StatusEnum;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobs_resumes")
public class JobResume implements Serializable {
    @EmbeddedId
    private JobResumeId id = new JobResumeId();

    @ManyToOne
    @MapsId("resumeId")
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private Instant updatedAt, createdAt;

    public JobResumeId getId() {
        return id;
    }

    public void setId(JobResumeId id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @PrePersist
    public void onPersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public ResponseResumeDTO convertResponseResumeDto() {
        ResponseResumeDTO responseResumeDTO = new ResponseResumeDTO();
        responseResumeDTO.setId(this.getResume().getId());
        responseResumeDTO.setUrl(this.getResume().getUrl());

        ResponseJobResumeDTO responseJobResumeDTO = new ResponseJobResumeDTO();
        responseJobResumeDTO.setId(this.getJob().getId());
        responseJobResumeDTO.setName(this.getJob().getName());
        responseJobResumeDTO.setLocation(this.getJob().getLocation());
        responseJobResumeDTO.setStatus(this.getStatus().name());
        responseJobResumeDTO.setUpdatedAt(this.getUpdatedAt());
        responseJobResumeDTO.setCreatedAt(this.getCreatedAt());
        responseJobResumeDTO.setLevel(this.getJob().getLevel());

        ResponseCompanyResumeDTO responseCompanyDTO = new ResponseCompanyResumeDTO();
        responseCompanyDTO.setName(this.getJob().getCompany().getName());
        responseCompanyDTO.setLogo(this.getJob().getCompany().getLogo());
        responseCompanyDTO.setAddress(this.getJob().getCompany().getAddress());

        responseJobResumeDTO.setCompany(responseCompanyDTO);
        responseResumeDTO.setJob(responseJobResumeDTO);
        return responseResumeDTO;
    }

}
