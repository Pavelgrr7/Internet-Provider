package com.pavelryzh.provider.service;


import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface AdditionalServiceService {

    AdditionalServiceResponseDto create(Long boundedTariffId, String serviceName, BigDecimal serviceCost);

    List<AdditionalServiceResponseDto> getAll();
}
