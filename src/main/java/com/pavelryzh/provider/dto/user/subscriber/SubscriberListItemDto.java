package com.pavelryzh.provider.dto.user.subscriber;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class SubscriberListItemDto {
    private Long id;
    private String passportSeriesNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String login;
    private String email;
    private String phoneNumber;
    private List<ContractInfo> contracts;
}

