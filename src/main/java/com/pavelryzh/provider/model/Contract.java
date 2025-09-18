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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

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
            joinColumns = @JoinColumn(name = "contract_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id") // поле service id ссылается на сущность AdditionalService
    )
    private List<AdditionalService> services;
}
