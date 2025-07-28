package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long>{

    boolean existsByName(String name);

    @Query("SELECT t FROM Tariff t LEFT JOIN FETCH t.availableServices WHERE t.id = :tariffId")
    Optional<Tariff> findByIdWithAvailableServices(@Param("tariffId") Long tariffId);

    @Query("SELECT t FROM Tariff t where YEAR(t.startDate) <= :year")
    List<Tariff> findAllActiveByYear(Integer year);

}
