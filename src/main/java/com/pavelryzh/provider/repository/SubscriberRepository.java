package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>{
    Optional<Subscriber> findByLogin(String login);
    boolean existsByLogin(String name);


//    void updateSubscriberByEmail(String email);
//
//    void updateSubscriberByPassword(String hashedPassword);
}