package com.pavelryzh.provider.dto.user.subscriber;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriberCreateDto {
    @Id
    private BigDecimal id;

    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(max = 50, message = "Имя абонента не может превышать 50 символов")
    private String firstName;

    @NotBlank(message = "Необходимо указать заявленную скорость")
    @Size(max = 50,  message = "Фамилия абонента не может превышать 50 символов")
    private String middleName;

    @Size(max = 50, message = "Отчество абонента не может превышать 50 символов")
    private String lastName;

    private String login;

    private String password;

    private String role;

    public CharSequence getRawPassword() {
        return password;
    }
}
