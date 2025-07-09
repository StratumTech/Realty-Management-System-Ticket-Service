package com.stratumtech.realtyticket.controller;

import com.stratumtech.realtyticket.dto.UserRegistrationDTO;
import com.stratumtech.realtyticket.service.RegistrationService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO dto) {
        return ResponseEntity.ok(registrationService.register(dto));
    }
} 