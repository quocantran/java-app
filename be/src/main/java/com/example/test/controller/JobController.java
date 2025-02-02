package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Job;
import com.example.test.domain.request.job.CreateJobDTO;
import com.example.test.domain.request.job.UpdateJobDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseString;
import com.example.test.domain.response.job.ResponseJobDTO;
import com.example.test.service.JobService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("")
    public ResponseEntity<CreateJobDTO> create(@Valid @RequestBody CreateJobDTO job) throws BadRequestException {

        CreateJobDTO entity = this.jobService.create(job);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getJobs(@Filter Specification<Job> spec, Pageable pageable) {
        return new ResponseEntity<>(this.jobService.getJobs(spec, pageable), HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateJobDTO> update(@PathVariable String id, @RequestBody UpdateJobDTO job)
            throws BadRequestException {
        UpdateJobDTO entity = this.jobService.update(id, job);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<ResponseJobDTO> getJobById(@PathVariable String id) throws BadRequestException {
        return new ResponseEntity<>(this.jobService.getJobById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseString> delete(@PathVariable String id) throws BadRequestException {
        this.jobService.delete(id);
        return new ResponseEntity<>(new ResponseString("Delete success"), HttpStatus.OK);
    }

}
