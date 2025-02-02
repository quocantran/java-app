package com.example.test.domain.request.resume;

import jakarta.validation.constraints.NotBlank;

public class CreateResumeDTO {
    @NotBlank(message = "url is required")
    private String url;

    public String getUrl() {
        return url;
    }

}
