package com.stratumtech.realtyticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String role; // "AGENT" или "REGIONAL_ADMIN"
    private Integer regionId; // Выбор региона
    private String referralCode; // Опциональный реферальный код для AGENT
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;

}