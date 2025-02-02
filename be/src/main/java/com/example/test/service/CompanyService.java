package com.example.test.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.User;
import com.example.test.domain.request.company.CreateCompanyDTO;
import com.example.test.domain.request.company.FollowCompanyDTO;
import com.example.test.domain.request.company.UpdateCompanyDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationCompanyDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.company.ResponseCompanyUserDTO;
import com.example.test.repository.CompanyRepository;
import com.example.test.repository.UserRepository;
import com.google.gson.reflect.TypeToken;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public CreateCompanyDTO createCompany(CreateCompanyDTO company) throws BadRequestException {
        if (this.companyRepository.findByName(company.getName()) != null) {
            throw new BadRequestException("Company name already exists");
        }

        Company newCompany = new Company();

        newCompany.setName(company.getName());
        newCompany.setAddress(company.getAddress());
        newCompany.setLogo(company.getLogo());
        newCompany.setDescription(company.getDescription());

        Company res = this.companyRepository.save(newCompany);
        return company;
    }

    public ResponsePaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> company = this.companyRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(company.getNumber() + 1);
        meta.setPageSize(company.getSize());

        meta.setPages(company.getTotalPages());
        meta.setTotal(company.getTotalElements());

        List<ResponsePaginationCompanyDTO> companies = this.modelMapper.map(company.getContent(),
                new TypeToken<List<ResponsePaginationCompanyDTO>>() {
                }.getType());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(companies);

        return resultPaginationDTO;

    }

    public Company getById(String id) {
        return this.companyRepository.findById(Long.parseLong(id));
    }

    public Long countCompanies() {
        return this.companyRepository.count();
    }

    public Long countCompanyByWeek() {
        return this.companyRepository.countCompanyPast1Week();
    }

    public Long countCompanyByMonth() {
        return this.companyRepository.countCompanyPast1Month();
    }

    public Long countCompanyByYear() {
        return this.companyRepository.countCompanyPast1Year();
    }

    public UpdateCompanyDTO updateCompany(String id, UpdateCompanyDTO company) throws BadRequestException {
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            throw new BadRequestException("Id must be a number");
        }

        Company current = companyRepository.findById(Long.parseLong(id));
        if (current == null) {
            throw new BadRequestException("Company not found");
        }

        modelMapper.map(company, current);
        this.companyRepository.save(current);
        modelMapper.map(current, company);

        return company;

    }

    public ResponseCompanyUserDTO getCompanyById(String id) throws BadRequestException {

        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id must be a number");
        }

        Company company = this.companyRepository.findById(Long.parseLong(id));
        if (company == null) {
            throw new BadRequestException("Company not found");
        }
        return company.convertResponseCompanyUserDTO();
    }

    public void deleteCompany(String id) throws BadRequestException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id must be a number");
        }
        Company company = this.companyRepository.findById(Long.parseLong(id));
        if (company == null) {
            throw new BadRequestException("Company not found");
        }

        for (User user : company.getUsers()) {
            user.setCompany(null);
        }

        this.companyRepository.deleteById(Long.parseLong(id));
    }

    public void followCompany(FollowCompanyDTO entity) throws BadRequestException {
        try {
            Long.parseLong(entity.getCompanyId());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id not found");
        }

        Company company = this.companyRepository.findById(Long.parseLong(entity.getCompanyId()));

        if (company == null) {
            throw new BadRequestException("Company not found");
        }

        User user = this.userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.getFollowedCompanies().contains(company)) {
            throw new BadRequestException("Company already followed");
        }

        List<Company> companies = user.getFollowedCompanies();
        companies.add(company);
        user.setFollowedCompanies(companies);

        this.userRepository.save(user);

    }

    public void unFollowCompany(FollowCompanyDTO entity) throws BadRequestException {
        try {
            Long.parseLong(entity.getCompanyId());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id not found");
        }

        Company company = this.companyRepository.findById(Long.parseLong(entity.getCompanyId()));

        if (company == null) {
            throw new BadRequestException("Company not found");
        }

        User user = this.userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!user.getFollowedCompanies().contains(company)) {
            throw new BadRequestException("Company not followed");
        }

        List<Company> companies = user.getFollowedCompanies();
        companies.remove(company);
        user.setFollowedCompanies(companies);

        this.userRepository.save(user);

    }
}
