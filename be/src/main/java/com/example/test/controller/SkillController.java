package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Skill;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.service.SkillService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {
    private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getAllSkills(@Filter Specification<Skill> spec, Pageable pageable) {
        ResponsePaginationDTO res = this.skillService.getAllSkills(spec, pageable);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill skill) throws BadRequestException {

        Skill entity = this.skillService.create(skill);

        return new ResponseEntity<Skill>(entity, HttpStatus.CREATED);
    }

    @PatchMapping("")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill entity) throws BadRequestException {
        Skill skill = this.skillService.findSkillById(entity.getId());
        if (skill == null) {
            throw new BadRequestException("Skill not found");
        }
        Skill updatedSkill = this.skillService.update(entity);
        return new ResponseEntity<Skill>(updatedSkill, HttpStatus.OK);
    }

}
