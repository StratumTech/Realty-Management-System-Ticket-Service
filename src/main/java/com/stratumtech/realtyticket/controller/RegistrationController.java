package com.stratumtech.realtyticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stratumtech.realtyticket.dto.UserRegistrationDTO;
import com.stratumtech.realtyticket.service.RegistrationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO dto) {
        return ResponseEntity.ok(registrationService.register(dto));
    }
} 