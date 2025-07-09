package com.stratumtech.realtyticket.service;

import com.stratumtech.realtyticket.dto.UserRegistrationDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RegistrationService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RegistrationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Map<String, Object> register(UserRegistrationDTO dto) {
        UUID requestId = UUID.randomUUID();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id", requestId);
        requestMap.put("status", "PENDING");
        requestMap.put("role", dto.getRole());
        requestMap.put("regionId", dto.getRegionId());
        requestMap.put("referralCode", dto.getReferralCode());
        requestMap.put("name", dto.getName());
        requestMap.put("patronymic", dto.getPatronymic());
        requestMap.put("surname", dto.getSurname());
        requestMap.put("email", dto.getEmail());
        requestMap.put("phone", dto.getPhone());
        requestMap.put("telegramTag", dto.getTelegramTag());
        requestMap.put("preferChannel", dto.getPreferChannel());

        String redisKey = "admin_request:" + requestId;
        redisTemplate.opsForValue().set(redisKey, requestMap);

        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("status", "PENDING");
        return response;
    }
} 