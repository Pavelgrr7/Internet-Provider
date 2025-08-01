package com.pavelryzh.provider.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Tariffs")
@Data // Используем Lombok для краткости
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tariff_name", nullable = false, unique = true)
    private String name;

    @Column(name = "declared_speed", nullable = false)
    private String declaredSpeed;

    @Column(name = "installation_fee", nullable = false)
    private BigDecimal installationFee;

    @Column(name = "ip_address_type", nullable = false)
    private String ipAddressType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tariff_services",
            joinColumns = @JoinColumn(name = "tariff_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<AdditionalService> availableServices;

}
