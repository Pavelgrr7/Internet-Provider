package com.pavelryzh.provider.dto.mapper;

import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.model.AdditionalService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AdditionalServiceMapper {

    //  утилитарный класс -> приватный конструктор
    private AdditionalServiceMapper() {}

    public static AdditionalServiceResponseDto toDto(AdditionalService service) {
        if (service == null) {
            return null;
        }

        return new AdditionalServiceResponseDto(
        service.getServiceId(),
        service.getServiceName(),
        service.getCost()
        );
    }

    public static List<AdditionalServiceResponseDto> toDtoList(List<AdditionalService> services) {
        if (services == null) {
            return Collections.emptyList();
        }
        return services.stream()
                .map(AdditionalServiceMapper::toDto)
                .collect(Collectors.toList());
    }
}
