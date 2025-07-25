package com.pavelryzh.provider.model;

// в пакете model
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {

    @Override
    public String getRole() {
        return "ROLE_ADMIN";
    }
}