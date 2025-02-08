package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.test.domain.Notification;
import com.example.test.domain.Permission;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> ,JpaSpecificationExecutor<Notification>{
    
}
