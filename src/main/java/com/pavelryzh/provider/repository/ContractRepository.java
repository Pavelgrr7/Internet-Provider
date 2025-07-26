package com.pavelryzh.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pavelryzh.provider.model.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    // findBy[ИмяСвязанногоОбъекта]_[ИмяПоляВнутриНего]
    List<Contract> findBySubscriber_Id(Long subscriberId);

    @Query("SELECT c FROM Contract c LEFT JOIN FETCH c.services WHERE c.subscriber.id = :subscriberId")
    List<Contract> findAllBySubscriberIdWithServices(@Param("subscriberId") Long subscriberId);
}
