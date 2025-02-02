package com.example.test.domain.response.resume;

import java.time.Instant;
import java.util.List;

public class ResponseResumeDTO {
    private Long id;

    private String url;

    private ResponseJobResumeDTO job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResponseJobResumeDTO getJob() {
        return job;
    }

    public void setJob(ResponseJobResumeDTO job) {
        this.job = job;
    }

}