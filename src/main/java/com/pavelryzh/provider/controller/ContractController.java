package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.service.ContractService;
import lombok.extern.slf4j.Slf4j;
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

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
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
}
