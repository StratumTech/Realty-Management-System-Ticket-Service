package com.stratumtech.realtyticket.dto;

import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgentApprovalDTO extends BaseUserApprovalDTO {
    private String referralCode; // Из заявки (может быть null)
    private Integer regionId; // Регион агента
    private UUID approverAdminUuid; // UUID администратора, который одобрил заявку
}