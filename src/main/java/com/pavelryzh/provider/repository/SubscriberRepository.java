package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>{
    Optional<Subscriber> findByEmail(String email);

    boolean existsByEmail(String name);
}