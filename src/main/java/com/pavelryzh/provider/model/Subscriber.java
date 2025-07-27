package com.pavelryzh.provider.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // Важно для Lombok при наследовании
@Entity
@DiscriminatorValue("ROLE_USER")
public class Subscriber extends User {

    // Уникальные поля для абонента
    @Column(name = "passport_series_number", unique = true)
    private String passportSeriesNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Override
    public String getRole() {
        return "ROLE_USER";
    }
}