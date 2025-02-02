package com.example.test.domain.request.job;

import java.time.Instant;
import java.util.List;

import com.example.test.domain.Company;
import com.example.test.utils.constant.LevelEnum;

import jakarta.validation.constraints.NotNull;

public class CreateJobDTO extends UpdateJobDTO {
    @NotNull(message = "Company is required")
    private Long company;

    public CreateJobDTO(String name, String location, LevelEnum level, Double salary, Integer quantity,
            String description,
            Boolean active, List<Long> skills, Instant startDate, Instant endDate, Long company) {
        super(name, location, level, salary, quantity, description, active, skills, startDate, endDate);
        this.company = company;
    }

    public Long getcompany() {
        return company;
    }

    public void setcompany(Long company) {
        this.company = company;
    }

}
