package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<AdditionalService, Long> {

    Optional<AdditionalService> findByServiceId(long id);
    void deleteByServiceId(long id);
}
