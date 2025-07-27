package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.Tariff;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pavelryzh.provider.model.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    // findBy[ИмяСвязанногоОбъекта]_[ИмяПоляВнутриНего]
    List<Contract> findBySubscriber_Id(Long subscriberId);

    @Query("SELECT c FROM Contract c LEFT JOIN FETCH c.services WHERE c.subscriber.id = :subscriberId")
    List<Contract> findAllBySubscriberIdWithServices(@Param("subscriberId") Long subscriberId);

    @Query("SELECT c FROM Contract c " +
            "WHERE c.tariff.id = :tariffId " +
            "AND YEAR(c.signingDate) <= :year ")
    List<Contract> findActiveContractsByTariffAndYear(@Param("tariffId") Long tariffId, @Param("year") Integer year);

    Optional<Contract> findByIdAndSubscriberId(Long contractId, Long userId);
    @Query("SELECT c FROM Contract c LEFT JOIN FETCH c.services WHERE c.id = :contractId")
    Optional<Contract> findContractByIdWithServices(@Param("contractId") Long contractId);
}
