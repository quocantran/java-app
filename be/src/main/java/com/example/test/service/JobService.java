package com.example.test.service;

import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.Job;
import com.example.test.repository.JobRepository;
import com.example.test.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.test.domain.Skill;
import com.example.test.domain.User;
import com.example.test.domain.request.job.CreateJobDTO;
import com.example.test.domain.request.job.UpdateJobDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.job.ResponseJobDTO;
import com.example.test.domain.response.user.ResponseUserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillService skillService;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, SkillService skillService, CompanyService companyService,
            ModelMapper modelMapper, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public CreateJobDTO create(CreateJobDTO job) throws BadRequestException {

        List<Skill> skills = this.skillService.findSkillByIdIn(job.getSkills());
        job.setSkills(skills.stream().map(Skill::getId).collect(Collectors.toList()));
        Company company = this.companyService.getById(String.valueOf(job.getcompany()));
        if (company == null) {
            throw new BadRequestException("Company not found");
        }

        if (this.jobRepository.existsByNameAndCompany(job.getName(), company)) {
            throw new BadRequestException("Job already exists");
        }

        Job entity = Job.convertToJob(job);
        entity.setCompany(company);
        entity.setSkills(skills);
        this.jobRepository.save(entity);

        return job;
    }

    public Boolean findJobByNameAndCompany(String name, Company company) {
        return this.jobRepository.existsByNameAndCompany(name, company);
    }

    public Job getById(String id) {
        return this.jobRepository.findById(Long.parseLong(id));
    }

    public Long countJobs() {
        return this.jobRepository.count();
    }

    public Long countJobByWeek() {
        return this.jobRepository.countJobPast1Week();
    }

    public Long countJobByMonth() {
        return this.jobRepository.countJobPast1Month();
    }

    public Long countJobByYear() {
        return this.jobRepository.countJobPast1Year();
    }

    public ResponsePaginationDTO getJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> job = this.jobRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(job.getNumber() + 1);
        meta.setPageSize(job.getSize());

        meta.setPages(job.getTotalPages());
        meta.setTotal(job.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        resultPaginationDTO.setResult(job.getContent().stream().map(jobEntity -> {
            ResponseJobDTO jobDTO = modelMapper.map(jobEntity, ResponseJobDTO.class);
            return jobDTO;

        }).collect(Collectors.toList()));
        return resultPaginationDTO;
    }

    public UpdateJobDTO update(String id, UpdateJobDTO job) throws BadRequestException {

        try {
            long tmp = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Job not found");
        }

        Job entity = this.jobRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Job not found");
        }
        modelMapper.map(job, entity);

        List<Long> ids = job.getSkills();
        List<Skill> skills = this.skillService.findSkillByIdIn(ids);
        entity.setSkills(skills.size() > 0 ? skills : entity.getSkills());

        this.jobRepository.save(entity);

        return job;
    }

    public ResponseJobDTO getJobById(String id) throws BadRequestException {
        try {
            long tmp = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Job not found");
        }
        Job entity = this.jobRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Job not found");
        }

        ResponseJobDTO jobDTO = modelMapper.map(entity, ResponseJobDTO.class);

        return jobDTO;
    }

    public void delete(String id) throws BadRequestException {
        try {
            long tmp = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Job not found");
        }

        Job entity = this.jobRepository.findById(Long.parseLong(id));

        if (entity == null) {
            throw new BadRequestException("Job not found");
        }

        this.jobRepository.delete(entity);

    }

    @Transactional
    public void addPaidUser(String jobId, String userId) {
        Job job = this.jobRepository.findById(Long.parseLong(jobId));
        User user = this.userRepository.findById(Long.parseLong(userId));
        List<User> paidUsers = job.getUsers();
        if (paidUsers.stream().anyMatch(u -> u.getId() == user.getId())) {
            return;
        }
        paidUsers.add(user);
        job.setUsers(paidUsers);
        this.jobRepository.save(job);
    }

}
