package com.example.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.test.domain.Subscriber;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>, JpaSpecificationExecutor<Subscriber> {

    Subscriber save(Subscriber subscriber);

    Subscriber findById(long id);

    Subscriber findByName(String name);

    Subscriber findByEmail(String email);

    Boolean existsById(long id);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);

}
