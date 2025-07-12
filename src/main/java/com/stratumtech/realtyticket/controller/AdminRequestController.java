package com.stratumtech.realtyticket.controller;

import com.stratumtech.realtyticket.service.AdminRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable UUID id,
            @RequestHeader("X-User-Uuid") UUID approverUuid) {
        
        boolean success = adminRequestService.approveRequest(id, approverUuid);
        if (success) {
            return ResponseEntity.ok("Request approved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to approve request");
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable UUID id) {
        
        boolean success = adminRequestService.rejectRequest(id);
        if (success) {
            return ResponseEntity.ok("Request rejected successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to reject request");
        }
    }
} 