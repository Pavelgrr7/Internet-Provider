package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long>{

    boolean existsByName(String name);

}
