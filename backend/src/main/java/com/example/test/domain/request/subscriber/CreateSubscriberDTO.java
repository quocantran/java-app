package com.example.test.domain.request.subscriber;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateSubscriberDTO {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    private String email;

    @NotNull(message = "skills is required")
    private List<Long> skills;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getSkills() {
        return skills;
    }

    public void setSkills(List<Long> skills) {
        this.skills = skills;
    }

}
