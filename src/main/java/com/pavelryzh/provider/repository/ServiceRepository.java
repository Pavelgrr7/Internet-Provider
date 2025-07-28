package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<AdditionalService, Long> {

    Optional<AdditionalService> findByServiceId(long id);
    void deleteByServiceId(long id);
}
