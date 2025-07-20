package com.stratumtech.realtyticket.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegionalAdminApprovalDTO extends BaseUserApprovalDTO {
    private Integer regionId; // Регион администратора
    private String referralCode; // Реферальный код для связи с агентами
} 