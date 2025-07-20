package com.stratumtech.realtyticket.producer;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.stratumtech.realtyticket.dto.RegionalAdminApprovalDTO;

@Component
@RequiredArgsConstructor
public class AdminApprovalProducer {

    @Value("${kafka.topic.regional-admin-approval-topic}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendRegionalAdminApproval(RegionalAdminApprovalDTO regionalAdminApprovalDTO) {
        kafkaTemplate.send(topic, regionalAdminApprovalDTO);
    }

}
