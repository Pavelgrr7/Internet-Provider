package com.pavelryzh.provider.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Contracts")
@Getter
@Setter
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String contractNumber;

    @NotNull
    private Long tariffId;

    @NotNull
    private String serviceAddress;

    private LocalDate signingDate;

    @NotNull
    private LocalDate serviceStartDate;

    @NotNull
    private BigDecimal monthlyFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private Subscriber subscriber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "contract_services",  // связующая таблица
            joinColumns = @JoinColumn(name = "contract_id"), // поле contract id ссылается на ТЕКУЩУЮ сущность (Contract)
            inverseJoinColumns = @JoinColumn(name = "service_id") // поле service id на ДРУГУЮ сущность (AdditionalService)
    )
    private List<AdditionalService> services;
}
