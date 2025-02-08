package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.JobResume;
import com.example.test.domain.Resume;
import com.example.test.domain.request.resume.ApplyResumeDTO;
import com.example.test.domain.request.resume.CreateResumeDTO;
import com.example.test.domain.request.resume.ResumeByJobDTO;
import com.example.test.domain.request.resume.UpdateResumeDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.resume.ResponseResumeDTO;
import com.example.test.service.ResumeService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("")
    public ResponsePaginationDTO getResumes(@Filter Specification<JobResume> spec, Pageable pageable) {
        return this.resumeService.getResumes(spec, pageable);
    }

    @PostMapping("")
    public ResponseEntity<ApplyResumeDTO> applyResume(@Valid @RequestBody ApplyResumeDTO entity)
            throws BadRequestException {

        return new ResponseEntity<>(this.resumeService.applyResume(entity), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResumeDTO> update(@PathVariable String id, @RequestBody UpdateResumeDTO entity)
            throws BadRequestException {

        return new ResponseEntity<>(this.resumeService.update(id, entity), HttpStatus.OK);
    }

    @GetMapping("/by-user")
    public ResponseEntity<ResponsePaginationDTO> getByUser(@Filter Specification<JobResume> spec, Pageable pageable) {

        return new ResponseEntity<>(this.resumeService.getByUser(spec, pageable), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateResumeDTO> createNewResume(@RequestBody CreateResumeDTO entity) {

        return new ResponseEntity<>(this.resumeService.createNewResume(entity), HttpStatus.CREATED);
    }

    @PostMapping("/by-job")
    public ResponseEntity<ResponsePaginationDTO> getByJob(@Valid @RequestBody ResumeByJobDTO entity)
            throws BadRequestException {

        return new ResponseEntity<>(this.resumeService.getByJob(entity), HttpStatus.OK);
    }

}
