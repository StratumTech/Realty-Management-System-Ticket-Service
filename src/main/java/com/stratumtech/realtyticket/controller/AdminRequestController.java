package com.stratumtech.realtyticket.controller;

import com.stratumtech.realtyticket.service.AdminRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin-requests")
public class AdminRequestController {

    private final AdminRequestService adminRequestService;

    public AdminRequestController(AdminRequestService adminRequestService) {
        this.adminRequestService = adminRequestService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAdminRequests(
            @RequestHeader("X-Role") String userRole,
            @RequestHeader(value = "X-Region-Id", required = false) Integer userRegionId,
            @RequestHeader(value = "X-Referral-Code", required = false) String userReferralCode) {

        List<Map<String, Object>> requests = adminRequestService.getAdminRequests(userRole, userRegionId, userReferralCode);
        return ResponseEntity.ok(requests);
    }
} 