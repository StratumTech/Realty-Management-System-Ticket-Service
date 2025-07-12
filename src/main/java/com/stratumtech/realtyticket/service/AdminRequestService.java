package com.stratumtech.realtyticket.service;

import com.stratumtech.realtyticket.dto.AgentApprovalDTO;
import com.stratumtech.realtyticket.dto.RegionalAdminApprovalDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminRequestService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaProducerService kafkaProducerService;

    public AdminRequestService(RedisTemplate<String, Object> redisTemplate, KafkaProducerService kafkaProducerService) {
        this.redisTemplate = redisTemplate;
        this.kafkaProducerService = kafkaProducerService;
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