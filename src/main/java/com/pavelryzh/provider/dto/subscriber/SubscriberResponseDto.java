package com.pavelryzh.provider.dto.subscriber;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubscriberResponseDto {
    private long subscriberId;

    @Size(max = 50, message = "Имя абонента не может превышать 50 символов")
    private String firstName;

    @Size(max = 50,  message = "Фамилия абонента не может превышать 50 символов")
    private String middleName;

    @Size(max = 50, message = "Отчество абонента не может превышать 50 символов")
    private String lastName;

    private String login;

    private String email;

    private String role;
}
