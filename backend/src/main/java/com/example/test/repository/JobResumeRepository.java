package com.example.test.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.test.domain.JobResume;
import com.example.test.domain.JobResumeId;

public interface JobResumeRepository
        extends JpaRepository<JobResume, JobResumeId>, JpaSpecificationExecutor<JobResume> {

    JobResume getByJobId(Long jobId);

    boolean existsByJobIdAndResumeId(Long jobId, Long resumeId);

    void deleteByJobIdAndResumeId(Long jobId, Long resumeId);

    Page<JobResume> findAllByJobId(Long jobId, Pageable pageable);

    Page<JobResume> findAllByResumeUserEmail(String userEmail, Pageable pageable);
}
