package com.example.test.domain.request.resume;

import com.example.test.utils.constant.StatusEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UpdateResumeDTO {

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private Long jobId;

    public UpdateResumeDTO(StatusEnum status, Long jobId) {
        this.status = status;
        this.jobId = jobId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public UpdateResumeDTO() {
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
