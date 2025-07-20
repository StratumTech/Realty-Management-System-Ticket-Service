package com.stratumtech.realtyticket.producer;

import com.stratumtech.realtyticket.dto.AgentApprovalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgentApprovalProducer {

    @Value("${kafka.topic.agent-approval-topic}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAgentApproval(AgentApprovalDTO agentApprovalDTO) {
        kafkaTemplate.send(topic, agentApprovalDTO);
    }
}
