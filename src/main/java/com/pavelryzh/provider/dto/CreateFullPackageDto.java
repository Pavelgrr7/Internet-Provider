package com.pavelryzh.provider.dto;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractWithoutUserCreateDto;
import com.pavelryzh.provider.dto.user.UserCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFullPackageDto {
    @Valid // Включаем валидацию для вложенного объекта
    @NotNull
    private UserCreateDto subscriberData;

    @Valid
    @NotNull
    private ContractCreateDto contractData;
}
