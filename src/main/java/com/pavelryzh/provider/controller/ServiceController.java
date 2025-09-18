package com.pavelryzh.provider.controller;


import com.pavelryzh.provider.dto.service.AddNewServiceRequestDto;
import com.pavelryzh.provider.dto.service.AddServiceRequestDto;
import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.service.AdditionalServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    AdditionalServiceService additionalServiceService;

    public ServiceController(AdditionalServiceService additionalServiceService) {
        this.additionalServiceService = additionalServiceService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdditionalServiceResponseDto> create(@RequestBody AddNewServiceRequestDto request ) {
        var newService = additionalServiceService.create(request.getTariffId(), request.getServiceName(), request.getServiceCost());
        return ResponseEntity.ok(newService);
    }

    @GetMapping
    public ResponseEntity<List<AdditionalServiceResponseDto>> getAll() {
        var allServices = additionalServiceService.getAll();
        return ResponseEntity.ok(allServices);
    }

}
