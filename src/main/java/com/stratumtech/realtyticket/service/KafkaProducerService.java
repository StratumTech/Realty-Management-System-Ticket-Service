package com.stratumtech.realtyticket.service;

import com.stratumtech.realtyticket.dto.AgentApprovalDTO;
import com.stratumtech.realtyticket.dto.RegionalAdminApprovalDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String AGENT_APPROVAL_TOPIC = "agent-approval";
    private static final String REGIONAL_ADMIN_APPROVAL_TOPIC = "regional-admin-approval";

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAgentApproval(AgentApprovalDTO agentApprovalDTO) {
        kafkaTemplate.send(AGENT_APPROVAL_TOPIC, agentApprovalDTO);
    }

    public void sendRegionalAdminApproval(RegionalAdminApprovalDTO regionalAdminApprovalDTO) {
        kafkaTemplate.send(REGIONAL_ADMIN_APPROVAL_TOPIC, regionalAdminApprovalDTO);
    }
} 