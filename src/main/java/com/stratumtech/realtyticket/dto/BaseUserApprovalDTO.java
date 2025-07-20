package com.stratumtech.realtyticket.dto;

import lombok.Data;

@Data
public class BaseUserApprovalDTO {
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;
} 