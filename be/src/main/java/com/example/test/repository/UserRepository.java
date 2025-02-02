package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.test.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User save(User user);

    User findByEmail(String email);

    User findById(long id);

    Boolean existsById(long id);

    @Query(value = "SELECT COUNT(*) FROM USERS WHERE created_at <= CURRENT_DATE - INTERVAL 7 DAY", nativeQuery = true)
    Long countUsersPast1Week();

    @Query(value = "SELECT COUNT(*) FROM USERS WHERE created_at <=  CURRENT_DATE - INTERVAL 1 MONTH", nativeQuery = true)
    Long countUsersPast1Month();

    @Query(value = "SELECT COUNT(*) FROM USERS WHERE created_at <=  CURRENT_DATE - INTERVAL 1 YEAR ", nativeQuery = true)
    Long countUsersPast1Year();

}
