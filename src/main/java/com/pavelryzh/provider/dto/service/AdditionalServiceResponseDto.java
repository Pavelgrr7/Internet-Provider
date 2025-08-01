package com.pavelryzh.provider.dto.service;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class AdditionalServiceResponseDto {

    @NonNull
    private Long serviceId;

    @NonNull
    private String serviceName;

    @NonNull
    private BigDecimal cost;

}
