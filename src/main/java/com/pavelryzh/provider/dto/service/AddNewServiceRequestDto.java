package com.pavelryzh.provider.dto.service;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddNewServiceRequestDto {

    @NotNull
    private Long tariffId;

    private String serviceName;

    private BigDecimal serviceCost;

}
