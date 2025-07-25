package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String name);
}