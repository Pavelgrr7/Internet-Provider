package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.contract.ContractWithServicesDto;
import com.pavelryzh.provider.dto.service.AddServiceRequestDto;
import com.pavelryzh.provider.dto.tariff.ChangeTariffPreviewDto;
import com.pavelryzh.provider.dto.tariff.ChangeTariffRequestDto;
import com.pavelryzh.provider.dto.tariff.TariffSelectionDto;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.User;
import com.pavelryzh.provider.service.ContractService;
import com.pavelryzh.provider.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    ContractService contractService;
    UserService userService;

    public ContractController(ContractService contractService, UserService userService) {
        this.userService = userService;
        this.contractService = contractService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContractResponseDto> createContract(@Valid @RequestBody ContractCreateDto createDto) {
        ContractResponseDto newContract = contractService.create(createDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newContract);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContractResponseDto>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAll());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ContractResponseDto>> getMyContracts(Authentication authentication) {
        Subscriber currentUser = (Subscriber) authentication.getPrincipal();

        long currentUserId = currentUser.getId();

        List<ContractResponseDto> contracts = contractService.getContractsByUserId(currentUserId);
        log.info("Contracts response {}", contracts);
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/my/detailed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ContractWithServicesDto>> getMyContractDetails(Authentication authentication) {
        Subscriber currentUser = (Subscriber) authentication.getPrincipal();

        long currentUserId = currentUser.getId();

        List<ContractWithServicesDto> contracts = contractService.getContractsWithServicesForUser(currentUserId);
        log.info("Detailed contracts response {}", contracts);
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/{contractId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContractResponseDto> getContractById(@PathVariable Long contractId) {
        ContractResponseDto contract = contractService.getContractDtoById(contractId);
        log.info("Contract response {}", contract);
        return ResponseEntity.ok(contract);
    }

    @DeleteMapping("/{contractId}/services/{serviceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeServiceFromContract(
            Authentication authentication,
            @PathVariable Long contractId,
            @PathVariable Long serviceId) {

        User currentUser = (User) authentication.getPrincipal();

        contractService.removeServiceFromContract(currentUser.getId(), contractId, serviceId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{contractId}/services")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addServiceToContract(
            Authentication authentication,
            @PathVariable Long contractId,
            @Valid @RequestBody AddServiceRequestDto request) {

        User currentUser = (User) authentication.getPrincipal();
        contractService.addServiceToContract(currentUser.getId(), contractId, request.getServiceId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{contractId}/change-tariff-preview")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChangeTariffPreviewDto> getTariffChangePreview(
            Authentication authentication,
            @PathVariable Long contractId,
            @RequestParam Long newTariffId) {

        User currentUser = (User) authentication.getPrincipal();
        ChangeTariffPreviewDto preview = contractService.getTariffChangePreview(
                currentUser.getId(), contractId, newTariffId
        );
        return ResponseEntity.ok(preview);
    }

    @PatchMapping("/{contractId}/tariff")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContractWithServicesDto> changeTariff( Authentication authentication,
            @PathVariable Long contractId, @RequestBody ChangeTariffRequestDto request) {
        log.info("TariffId {} ", request.getTariffId());
        var currentUser = (User) authentication.getPrincipal();
        ContractWithServicesDto contract = contractService.changeTariff(currentUser.getId(), contractId, request.getTariffId());
        log.info("Tariff changed, new contract: {}", contract);
        return ResponseEntity.ok(contract);
    }

    @PatchMapping("/{contractId}/address")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changeAddress( Authentication authentication,
                                               @PathVariable Long contractId, @RequestBody String newAddress) {
        log.info("Address {} ", newAddress);
        var currentUser = (User) authentication.getPrincipal();
        contractService.changeAddress(currentUser.getId(), contractId, newAddress);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{contractId}/serviceStartDate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changeServiceStartDate( Authentication authentication,
                                               @PathVariable Long contractId, @RequestBody String newStartDate) {
        log.info("StartDate {} ", newStartDate);
        var currentUser = (User) authentication.getPrincipal();
        contractService.changeStartDate(currentUser.getId(), contractId, newStartDate);
        return ResponseEntity.noContent().build();
    }



}
