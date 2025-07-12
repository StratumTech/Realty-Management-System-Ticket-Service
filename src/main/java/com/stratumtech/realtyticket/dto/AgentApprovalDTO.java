package com.stratumtech.realtyticket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentApprovalDTO extends BaseUserApprovalDTO {
    private String referralCode; // Из заявки (может быть null)
    private Integer regionId; // Регион агента
    private UUID approverAdminUuid; // UUID администратора, который одобрил заявку
}