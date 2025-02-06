package com.example.test.domain.request.resume;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResumeByJobDTO {
    private String jobId;
    private Long page, size;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

}
