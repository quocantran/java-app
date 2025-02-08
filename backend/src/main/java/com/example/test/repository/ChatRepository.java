package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.test.domain.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, JpaSpecificationExecutor<Chat> {

    Chat findById(long id);

    Boolean existsById(long id);

}
