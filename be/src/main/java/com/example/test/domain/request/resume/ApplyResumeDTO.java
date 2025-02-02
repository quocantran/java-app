package com.example.test.domain.request.resume;

import com.example.test.utils.constant.StatusEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApplyResumeDTO {
    private String url;

    @NotBlank(message = "companyId is required")
    private String companyId;

    @NotBlank(message = "jobId is required")
    private String jobId;

    private String resumeId;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public ApplyResumeDTO(@NotBlank(message = "url is required") String url,
            @NotBlank(message = "companyId is required") String companyId,
            @NotBlank(message = "jobId is required") String jobId, StatusEnum status) {
        this.url = url;
        this.companyId = companyId;
        this.jobId = jobId;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

}
