package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.contract.ContractWithServicesDto;
import com.pavelryzh.provider.dto.service.AddServiceRequestDto;
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
}
