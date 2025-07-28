package com.pavelryzh.provider.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "services")
public class AdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long serviceId;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_cost")
    private BigDecimal cost;

    @ManyToMany(mappedBy = "availableServices") // mappedBy: связь уже настроена на другой стороне
    private List<Tariff> tariffs;

}
