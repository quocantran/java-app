package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.request.company.CreateCompanyDTO;
import com.example.test.domain.request.company.FollowCompanyDTO;
import com.example.test.domain.request.company.UpdateCompanyDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseString;
import com.example.test.domain.response.company.ResponseCompanyUserDTO;
import com.example.test.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getAllCompanies(@Filter Specification<Company> spec,
            Pageable pageable) {

        ResponsePaginationDTO companies = this.companyService.getAllCompanies(spec, pageable);
        return new ResponseEntity<ResponsePaginationDTO>(companies, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateCompanyDTO> updateCompany(@PathVariable String id,
            @RequestBody UpdateCompanyDTO company)
            throws BadRequestException {
        UpdateCompanyDTO res = this.companyService.updateCompany(id, company);
        return new ResponseEntity<UpdateCompanyDTO>(res, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateCompanyDTO> createCompany(@Valid @RequestBody CreateCompanyDTO company)
            throws BadRequestException {
        CreateCompanyDTO res = this.companyService.createCompany(company);
        return new ResponseEntity<CreateCompanyDTO>(res, HttpStatus.CREATED);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<ResponseCompanyUserDTO> getById(@PathVariable String id) throws BadRequestException {
        return new ResponseEntity<ResponseCompanyUserDTO>(this.companyService.getCompanyById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseString> deleteCompany(@PathVariable String id) throws BadRequestException {
        this.companyService.deleteCompany(id);
        return new ResponseEntity<>(new ResponseString("Delete success"), HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<ResponseString> followCompany(@RequestBody FollowCompanyDTO companyId)
            throws BadRequestException {
        this.companyService.followCompany(companyId);
        return ResponseEntity.ok().body(new ResponseString("Company followed"));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<ResponseString> unFollowCompany(@RequestBody FollowCompanyDTO companyId)
            throws BadRequestException {
        this.companyService.unFollowCompany(companyId);
        return ResponseEntity.ok().body(new ResponseString("Company followed"));
    }

}
