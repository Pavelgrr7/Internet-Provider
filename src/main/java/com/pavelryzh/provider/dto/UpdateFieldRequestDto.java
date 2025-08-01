package com.pavelryzh.provider.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateFieldRequestDto {
    @NotBlank
    private String value;
}