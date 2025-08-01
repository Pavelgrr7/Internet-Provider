package com.pavelryzh.provider.service;


import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;

import java.math.BigDecimal;

public interface AdditionalServiceService {

    AdditionalServiceResponseDto create(Long boundedTariffId, String serviceName, BigDecimal serviceCost);

}
