package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.example.test.domain.Skill;
import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    Skill save(Skill skill);

    Skill findById(long id);

    Skill findByName(String name);

    Boolean existsById(long id);

    List<Skill> findByIdIn(List<Long> skill);

}
