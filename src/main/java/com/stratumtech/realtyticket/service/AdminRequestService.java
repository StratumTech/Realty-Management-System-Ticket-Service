package com.stratumtech.realtyticket.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminRequestService {

    private final RedisTemplate<String, Object> redisTemplate;

    public AdminRequestService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<Map<String, Object>> getAdminRequests(String userRole, Integer userRegionId, String userReferralCode) {
        List<Map<String, Object>> allRequests = redisTemplate.keys("admin_request:*")
                .stream()
                .map(key -> (Map<String, Object>) redisTemplate.opsForValue().get(key))
                .filter(request -> request != null && "PENDING".equals(request.get("status")))
                .collect(Collectors.toList());

        return allRequests.stream()
                .filter(request -> hasAccess(request, userRole, userRegionId, userReferralCode))
                .collect(Collectors.toList());
    }

    private boolean hasAccess(Map<String, Object> request, String userRole,
                              Integer userRegionId, String userReferralCode) {
        String requestRole = (String) request.get("role");

        if ("REGIONAL_ADMIN".equals(requestRole)) {
            return "ADMIN".equals(userRole);
        }

        if ("AGENT".equals(requestRole)) {
            if (!"REGIONAL_ADMIN".equals(userRole)) {
                return false;
            }

            String requestReferralCode = (String) request.get("referralCode");
            Integer requestRegionId = (Integer) request.get("regionId");

            if (requestReferralCode != null && !requestReferralCode.isEmpty()) {
                return requestReferralCode.equals(userReferralCode);
            }

            return requestRegionId != null && requestRegionId.equals(userRegionId);
        }

        return false;
    }
} 