package com.example.test.domain.response;

import java.util.List;

public class ResponseEmailJob {
    private Long id;
    private String name;
    private double salary;
    private CompanyEmail company;
    private List<SkillEmail> skills;

    public ResponseEmailJob(String name, double salary, CompanyEmail company, List<SkillEmail> skills) {
        this.name = name;
        this.salary = salary;
        this.company = company;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponseEmailJob() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public CompanyEmail getCompany() {
        return company;
    }

    public void setCompany(CompanyEmail company) {
        this.company = company;
    }

    public List<SkillEmail> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillEmail> skills) {
        this.skills = skills;
    }

    public static class CompanyEmail {
        private String name;

        public String getName() {
            return name;
        }

        public CompanyEmail(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SkillEmail {
        private String name;

        public SkillEmail() {
        }

        public String getName() {
            return name;
        }

        public SkillEmail(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
