package com.pavelryzh.provider.dto.user.admin;

import com.pavelryzh.provider.dto.contract.ContractWithServicesDto;
import lombok.Data;

import java.util.List;

@Data
public class AdminSubscriberDetailsDto {
    private Long id;
    private String fullName;
    private String passportSeriesNumber;
    private String phoneNumber;
    private String email;
    private String login;
    private List<ContractWithServicesDto> contracts; // Вложенный список договоров
}

