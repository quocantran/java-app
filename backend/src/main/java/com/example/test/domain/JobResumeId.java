package com.example.test.domain;

import java.io.Serializable;

public class JobResumeId implements Serializable {
    private Long resumeId;
    private Long jobId;

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

}
