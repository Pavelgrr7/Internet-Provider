package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffUpdateDto;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.service.TariffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    public ResponseEntity<TariffResponseDto> createTariff(@Valid @RequestBody TariffCreateDto createDto) {
        // 1. Принимаем TariffCreateDto. @Valid запускает валидацию.
        // 2. TariffService внутри себя превращает DTO в Entity и сохраняет в БД.
        // 3. TariffService возвращает нам DTO для ответа.
        TariffResponseDto createdTariff = tariffService.create(createDto);
        return new ResponseEntity<>(createdTariff, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TariffResponseDto>> getAllTariffs() {
        List<TariffResponseDto> tariffs = tariffService.getAll();
        return ResponseEntity.ok(tariffs);

    }


    @GetMapping("/{id}")
    public ResponseEntity<TariffResponseDto> getTariffById(@PathVariable Long id) {
            TariffResponseDto tariff = tariffService.getById(id);
            return ResponseEntity.ok(tariff);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<TariffResponseDto> updateTariffById(
            @PathVariable Long id,
            @Valid @RequestBody TariffUpdateDto updateDto) {

        TariffResponseDto updatedTariff = tariffService.update(id, updateDto);

        return ResponseEntity.ok(updatedTariff);
    }
}
