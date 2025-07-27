package com.pavelryzh.provider.dto.tariff;

import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ChangeTariffPreviewDto {
    private List<AdditionalServiceResponseDto> servicesToDisconnect;

    private BigDecimal newMonthlyFee;
}
