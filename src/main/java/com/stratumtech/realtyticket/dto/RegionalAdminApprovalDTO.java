package com.stratumtech.realtyticket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionalAdminApprovalDTO extends BaseUserApprovalDTO {
    private Integer regionId; // Регион администратора
    private String referralCode; // Реферальный код для связи с агентами
} 