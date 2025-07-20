package com.stratumtech.realtyticket.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import com.stratumtech.realtyticket.dto.AgentApprovalDTO;
import com.stratumtech.realtyticket.dto.RegionalAdminApprovalDTO;

import com.stratumtech.realtyticket.producer.AdminApprovalProducer;
import com.stratumtech.realtyticket.producer.AgentApprovalProducer;

@Service
@RequiredArgsConstructor
public class AdminRequestService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final AdminApprovalProducer adminApprovalProducer;
    private final AgentApprovalProducer agentApprovalProducer;
    private final ObjectMapper mapper;

    public List<Map<String, Object>> getAdminRequests() {
        return redisTemplate.keys("admin_request:*")
                .stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .map(obj -> mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {}))
                .filter(request -> request != null && "PENDING".equals(request.get("status")))
                .toList();
    }

    public boolean approveRequest(UUID requestId, UUID approverUuid) {
        String redisKey = "admin_request:" + requestId;
        Map<String, Object> request = (Map<String, Object>) redisTemplate.opsForValue().get(redisKey);
        
        if (request == null || !"PENDING".equals(request.get("status"))) {
            return false;
        }

        String role = (String) request.get("role");
        
        if ("AGENT".equals(role)) {
            AgentApprovalDTO agentApprovalDTO = new AgentApprovalDTO();
            agentApprovalDTO.setName((String) request.get("name"));
            agentApprovalDTO.setPatronymic((String) request.get("patronymic"));
            agentApprovalDTO.setSurname((String) request.get("surname"));
            agentApprovalDTO.setEmail((String) request.get("email"));
            agentApprovalDTO.setPhone((String) request.get("phone"));
            agentApprovalDTO.setTelegramTag((String) request.get("telegramTag"));
            agentApprovalDTO.setPreferChannel((String) request.get("preferChannel"));
            agentApprovalDTO.setReferralCode((String) request.get("referralCode"));
            agentApprovalDTO.setRegionId((Integer) request.get("regionId"));
            agentApprovalDTO.setApproverAdminUuid(approverUuid); // UUID администратора, который одобрил

            kafkaProducerService.sendAgentApproval(agentApprovalDTO);
            
        } else if ("REGIONAL_ADMIN".equals(role)) {
            RegionalAdminApprovalDTO regionalAdminApprovalDTO = new RegionalAdminApprovalDTO();
            regionalAdminApprovalDTO.setName((String) request.get("name"));
            regionalAdminApprovalDTO.setPatronymic((String) request.get("patronymic"));
            regionalAdminApprovalDTO.setSurname((String) request.get("surname"));
            regionalAdminApprovalDTO.setEmail((String) request.get("email"));
            regionalAdminApprovalDTO.setPhone((String) request.get("phone"));
            regionalAdminApprovalDTO.setTelegramTag((String) request.get("telegramTag"));
            regionalAdminApprovalDTO.setPreferChannel((String) request.get("preferChannel"));
            regionalAdminApprovalDTO.setRegionId((Integer) request.get("regionId"));
            regionalAdminApprovalDTO.setReferralCode((String) request.get("referralCode"));

            kafkaProducerService.sendRegionalAdminApproval(regionalAdminApprovalDTO);
        }

        redisTemplate.delete(redisKey);
        
        return true;
    }

    public boolean rejectRequest(UUID requestId) {
        String redisKey = "admin_request:" + requestId;
        Map<String, Object> request = (Map<String, Object>) redisTemplate.opsForValue().get(redisKey);
        
        if (request == null || !"PENDING".equals(request.get("status"))) {
            return false;
        }

        redisTemplate.delete(redisKey);
        
        return true;
    }
} 