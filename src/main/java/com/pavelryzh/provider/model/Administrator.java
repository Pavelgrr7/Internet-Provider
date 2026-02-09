package com.pavelryzh.provider.model;

// в пакете model

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ROLE_ADMIN")
public class Administrator extends User {

    @Override
    public String getRole() {
        return "ROLE_ADMIN";
    }
}