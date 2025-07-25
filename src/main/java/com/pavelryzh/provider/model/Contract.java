package com.pavelryzh.provider.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private Long subscriberId;

    @NotNull
    private Long tariffId;

    @NotNull
    private String serviceAddress;

    private LocalDate signingDate;

    @NotNull
    private LocalDate serviceStartDate;

    @NotNull
    private BigDecimal monthlyFee;

}
