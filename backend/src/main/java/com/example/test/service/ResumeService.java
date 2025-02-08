package com.example.test.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.Job;
import com.example.test.domain.JobResume;
import com.example.test.domain.Resume;
import com.example.test.domain.request.resume.ApplyResumeDTO;
import com.example.test.domain.request.resume.CreateResumeDTO;
import com.example.test.domain.request.resume.ResumeByJobDTO;
import com.example.test.domain.request.resume.UpdateResumeDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.resume.ResponseCompanyResumeDTO;
import com.example.test.domain.response.resume.ResponseJobResumeDTO;
import com.example.test.domain.response.resume.ResponseResumeDTO;
import com.example.test.domain.response.role.ResponseRoleDTO;
import com.example.test.repository.JobResumeRepository;
import com.example.test.repository.ResumeRepository;
import com.example.test.utils.constant.StatusEnum;

import jakarta.transaction.Transactional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final JobService jobService;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final JobResumeRepository jobResumeRepository;

    public ResumeService(ResumeRepository resumeRepository, JobService jobService, CompanyService companyService,
            ModelMapper modelMapper, UserService userService, JobResumeRepository jobResumeRepository) {
        this.resumeRepository = resumeRepository;
        this.jobService = jobService;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.jobResumeRepository = jobResumeRepository;
    }

    public ResponsePaginationDTO getResumes(Specification<JobResume> spec, Pageable pageable) {
        Page<JobResume> resumes = this.jobResumeRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(resumes.getNumber() + 1);
        meta.setPageSize(resumes.getSize());

        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        List<ResponseResumeDTO> responseResumesDTO = resumes.stream()
                .map(resume -> resume.convertResponseResumeDto())
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(responseResumesDTO);

        return resultPaginationDTO;

    }

    @Transactional
    public ApplyResumeDTO applyResume(ApplyResumeDTO entity) throws BadRequestException {
        Company company = this.companyService.getById(entity.getCompanyId());

        if (company == null) {
            throw new BadRequestException("Company not found");
        }

        Job job = this.jobService.getById(entity.getJobId());

        if (job == null) {
            throw new BadRequestException("Job not found");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (entity.getResumeId() == null) {
            Resume resume = new Resume();
            resume.setUrl(entity.getUrl());
            resume.setUser(this.userService.getUserByEmail(email));

            List<Company> companies = new ArrayList<>();
            companies.add(company);
            resume.setCompanies(companies);

            resume = this.resumeRepository.save(resume);

            JobResume jobResume = new JobResume();
            jobResume.setJob(job);
            jobResume.setResume(resume);
            jobResume.setStatus(StatusEnum.PENDING);

            this.jobResumeRepository.save(jobResume);
        } else {
            Resume resume = this.resumeRepository.findById(Long.parseLong(entity.getResumeId())).orElse(null);

            if (resume == null) {
                throw new BadRequestException("Resume not found");
            }

            List<Company> companies = resume.getCompanies();
            companies.add(company);
            resume.setCompanies(companies);

            JobResume jobResume = new JobResume();
            jobResume.setJob(job);
            jobResume.setResume(resume);
            jobResume.setStatus(StatusEnum.PENDING);

            if (this.jobResumeRepository.existsByJobIdAndResumeId(Long.parseLong(entity.getJobId()),
                    Long.parseLong(entity.getResumeId()))) {
                this.jobResumeRepository.deleteByJobIdAndResumeId(Long.parseLong(entity.getJobId()),
                        Long.parseLong(entity.getResumeId()));
            }
            this.jobResumeRepository.save(jobResume);
            this.resumeRepository.save(resume);
        }

        return entity;

    }

    public CreateResumeDTO createNewResume(CreateResumeDTO entity) {
        Resume resume = new Resume();
        resume.setUrl(entity.getUrl());
        resume.setUser(
                this.userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        resume = this.resumeRepository.save(resume);
        return entity;
    }

    public UpdateResumeDTO update(String id, UpdateResumeDTO entity) throws BadRequestException {

        try {
            Long.parseLong(id);
        } catch (Exception e) {
            throw new BadRequestException("not found resume");
        }
        Resume resume = this.resumeRepository.findById(Long.parseLong(id)).orElse(null);

        if (resume == null) {
            throw new BadRequestException("Resume not found");
        }
        boolean isExist = false;
        for (JobResume jobResume : resume.getJobs()) {
            if (jobResume.getJob().getId().equals(entity.getJobId())) {
                jobResume.setStatus(entity.getStatus());
                this.jobResumeRepository.save(jobResume);
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            throw new BadRequestException("Job not found");
        }

        return entity;
    }

    public ResponsePaginationDTO getByUser(Specification<JobResume> spec, Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<JobResume> resumes = this.jobResumeRepository.findAllByResumeUserEmail(username, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(resumes.getNumber() + 1);
        meta.setPageSize(resumes.getSize());

        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        List<ResponseResumeDTO> responseResumesDTO = resumes.stream()
                .map(resume -> resume.convertResponseResumeDto())
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(responseResumesDTO);

        return resultPaginationDTO;
    }

    public ResponsePaginationDTO getByJob(ResumeByJobDTO entity) throws BadRequestException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String jobId = entity.getJobId();
        Job job = this.jobService.getById(jobId);
        if (job == null) {
            throw new BadRequestException("Job not found");
        }
        if (job.getUsers().stream().noneMatch(user -> user.getEmail().equals(username))) {
            throw new BadRequestException("You are not paid for this job");
        }

        Pageable pageable = PageRequest.of(entity.getPage().intValue() - 1, entity.getSize().intValue());

        Page<JobResume> resumes = this.jobResumeRepository.findAllByJobId(Long.parseLong(jobId), pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();
        ResponseMetaDTO meta = new ResponseMetaDTO();
        meta.setCurrent(resumes.getNumber() + 1);
        meta.setPageSize(resumes.getSize());
        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        List<ResponseResumeDTO> responseResumesDTO = resumes.stream()
                .map(JobResume::convertResponseResumeDto)
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(responseResumesDTO);

        return resultPaginationDTO;
    }
}
