package com.pavelryzh.provider.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Subscriber {
    @Id
    private Long id;
}
