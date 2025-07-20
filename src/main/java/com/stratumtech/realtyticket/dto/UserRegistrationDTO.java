package com.stratumtech.realtyticket.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.stratumtech.realtyticket.validation.PhoneNumber;
import com.stratumtech.realtyticket.validation.TelegramTag;

@Data
public class UserRegistrationDTO {

    @NotNull
    @NotEmpty
    private String role;

    @NotNull
    private Long regionId;

    @NotNull
    @NotEmpty
    private String referralCode;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String patronymic;

    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @Email
    private String email;

    @NotNull
    @PhoneNumber
    private String phone;

    @NotNull
    @TelegramTag
    private String telegramTag;

    @NotNull
    @NotEmpty
    private String preferChannel;

    @NotNull
    @NotEmpty
    private String resume;

}