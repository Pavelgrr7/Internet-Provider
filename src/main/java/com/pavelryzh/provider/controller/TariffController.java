package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffSelectionDto;
import com.pavelryzh.provider.dto.tariff.TariffUpdateDto;
import com.pavelryzh.provider.service.TariffService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
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

    @GetMapping("/available-for-change")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TariffSelectionDto>> getTariffsAvailableForChange() {
        // Здесь мы вызываем специальный метод сервиса, который возвращает
        // легковесные DTO, а не полные TariffResponseDto.
        // Это эффективнее для выпадающего списка.
        List<TariffSelectionDto> tariffs = tariffService.findTariffsForSelection();
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

    @GetMapping("/available-for-contract/{contractId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AdditionalServiceResponseDto>> getAvailableServicesForContract(@PathVariable Long contractId) {

        log.info("Отправка доступных услуг для контракта {}: {}", contractId, tariffService.getAvailableServicesForContract(contractId));

        return ResponseEntity.ok(tariffService.getAvailableServicesForContract(contractId));
    }

    @GetMapping("/active-in-year")
    public ResponseEntity<List<TariffSelectionDto>> getActiveTariffs(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(tariffService.findActiveTariffs(year));
    }

    @GetMapping("/{tariffId}/active-years")
    public ResponseEntity<List<Integer>> getActiveYears(@PathVariable Long tariffId) {
        return ResponseEntity.ok(tariffService.findActiveYearsForTariff(tariffId));
    }

}
