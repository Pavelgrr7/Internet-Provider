package com.pavelryzh.provider.dto.service;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddServiceRequestDto {
    @NotNull
    private Long serviceId;
}