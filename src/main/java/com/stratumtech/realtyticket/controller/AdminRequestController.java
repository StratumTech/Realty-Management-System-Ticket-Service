package com.stratumtech.realtyticket.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;

import org.springframework.security.core.Authentication;

import com.stratumtech.realtyticket.service.ApproveRequestService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-requests")
public class AdminRequestController {

    private final ObjectMapper mapper;
    private final ApproveRequestService approveRequestService;

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<List<Map<String, Object>>> getRequests(Authentication authentication) {
        final var userRole = authentication.getAuthorities()
                .stream().findFirst().get().getAuthority();

        Object detailsObj = authentication.getDetails();

        List<Map<String, Object>> requests = approveRequestService.getRequests();

        if(userRole.endsWith("REGIONAL_ADMIN")){
            Map<String, Object> details =  mapper.convertValue(
                    detailsObj,
                    new TypeReference<>() { }
            );
            var regionId = (Long) details.get("adminRegionId");
            var referralCode = (String) details.get("adminReferralCode");
            requests = requests.stream().takeWhile(m ->
                    ((Long) m.get("regionId")).compareTo(regionId) == 0 &&
                    (m.get("referralCode").equals(referralCode) || m.get("referralCode") == null)
                ).collect(Collectors.toList());
        }else{
            requests = requests.stream().takeWhile(m -> ((String) m.get("role")).endsWith("REGIONAL_ADMIN"))
                            .collect(Collectors.toList());
            requests.addAll(approveRequestService.getRejectedRequests());
        }

        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{id}/approve")
    @Secured({"ROLE_ADMIN", "ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<?> approveRequest(@PathVariable UUID id,
                                            Authentication authentication) {
        return responseToRequest(
                id,
                authentication,
                (pair) -> approveRequestService.approveRequest(
                        pair.getFirst(),
                        pair.getSecond()
                )
        );
    }

    @PostMapping("/{id}/reject")
    @Secured({"ROLE_ADMIN", "ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<?> rejectRequest(@PathVariable UUID id,
                                           Authentication authentication) {
        return responseToRequest(
                id,
                authentication,
                (pair) -> approveRequestService.rejectRequest(
                        pair.getFirst(),
                        pair.getSecond()
                )
        );
    }

    private ResponseEntity<?> responseToRequest(UUID id,
                                                Authentication authentication,
                                                Function<Pair<UUID, UUID>, Boolean> func) {
        UUID respondentUuid = (UUID) authentication.getPrincipal();
        return func.apply(Pair.of(id, respondentUuid))
                ? ResponseEntity.ok("Request responded successfully")
                : ResponseEntity.badRequest().body("Failed to response request");
    }
} 