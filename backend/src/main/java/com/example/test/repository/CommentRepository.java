package com.example.test.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.test.domain.Comment;
import com.example.test.domain.Company;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("UPDATE Comment c SET c.right = c.right + 2 WHERE c.company = :company AND c.right >= :rightValue")
    void updateRightValues(Company company, int rightValue);

    @Modifying
    @Query("UPDATE Comment c SET c.left = c.left + 2 WHERE c.company = :company AND c.left > :rightValue")
    void updateLeftValues(Company company, int rightValue);

    @Query("SELECT MAX(c.right) FROM Comment c WHERE c.company = :company")
    Integer findMaxRightValue(Company company);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.company = :company AND c.left >= :leftValue AND c.right <= :rightValue")
    void deleteCommentsInRange(Company company, int leftValue, int rightValue);

    @Modifying
    @Query("UPDATE Comment c SET c.left = c.left - :width WHERE c.company = :company AND c.left > :rightValue")
    void updateLeftValuesAfterDelete(Company company, int rightValue, int width);

    @Modifying
    @Query("UPDATE Comment c SET c.right = c.right - :width WHERE c.company = :company AND c.right > :rightValue")
    void updateRightValuesAfterDelete(Company company, int rightValue, int width);

    Page<Comment> findByCompanyIdAndParentIsNull(Long companyId, Pageable pageable);

    Page<Comment> findByParentId(Long parentId, Pageable pageable);

    List<Comment> findByParentId(Long parentId);
}
