package com.pavelryzh.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pavelryzh.provider.model.Contract;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findBySubscriberId(Long subscriberId);
}
