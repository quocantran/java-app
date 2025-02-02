package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.test.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Company save(Company company);

    Company findByName(String name);

    Company findById(long id);

    Boolean existsById(long id);

    @Query(value = "SELECT COUNT(*) FROM COMPANIES WHERE created_at <= CURRENT_DATE - INTERVAL 7 DAY", nativeQuery = true)
    Long countCompanyPast1Week();

    @Query(value = "SELECT COUNT(*) FROM COMPANIES WHERE created_at <=  CURRENT_DATE - INTERVAL 1 MONTH", nativeQuery = true)
    Long countCompanyPast1Month();

    @Query(value = "SELECT COUNT(*) FROM COMPANIES WHERE created_at <=  CURRENT_DATE - INTERVAL 1 YEAR ", nativeQuery = true)
    Long countCompanyPast1Year();

}
