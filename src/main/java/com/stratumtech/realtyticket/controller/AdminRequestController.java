package com.stratumtech.realtyticket.controller;

import com.stratumtech.realtyticket.service.AdminRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/admin-requests")
public class AdminRequestController {

    private final AdminRequestService adminRequestService;

    public AdminRequestController(AdminRequestService adminRequestService) {
        this.adminRequestService = adminRequestService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAdminRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities().stream().findFirst().map(Object::toString).orElse(null);
        String userUuid = (String) authentication.getPrincipal();
        Object detailsObj = authentication.getDetails();
        Integer regionId = null;
        String referralCode = null;
        if (detailsObj instanceof Map) {
            Map<?, ?> details = (Map<?, ?>) detailsObj;
            regionId = (Integer) details.get("regionId");
            referralCode = (String) details.get("referralCode");
        }
        List<Map<String, Object>> requests = adminRequestService.getAdminRequests(userRole, regionId, referralCode);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String approverUuid = (String) authentication.getPrincipal();
        boolean success = adminRequestService.approveRequest(id, UUID.fromString(approverUuid));
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