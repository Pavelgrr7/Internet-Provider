package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffUpdateDto;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.service.TariffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponseDto> createTariff(@Valid @RequestBody TariffCreateDto createDto) {

        // 2. TariffService внутри себя превращает DTO в Entity и сохраняет в БД.
        // 3. TariffService возвращает нам DTO для ответа.

        TariffResponseDto createdTariff = tariffService.create(createDto);
        URI location = URI.create(String.format("/api/tariffs/%d", createdTariff.getId()));
        return ResponseEntity.created(location).body(createdTariff);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TariffResponseDto>> getAllTariffs() {
        List<TariffResponseDto> tariffs = tariffService.getAll();
        return ResponseEntity.ok(tariffs);

    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TariffResponseDto> getTariffById(@PathVariable Long id) {
            TariffResponseDto tariff = tariffService.getById(id);
            return ResponseEntity.ok(tariff);

    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponseDto> updateTariffById(
            @PathVariable Long id,
            @Valid @RequestBody TariffUpdateDto updateDto) {

        TariffResponseDto updatedTariff = tariffService.update(id, updateDto);

        return ResponseEntity.ok(updatedTariff);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTariffById(@PathVariable Long id) {
        tariffService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
