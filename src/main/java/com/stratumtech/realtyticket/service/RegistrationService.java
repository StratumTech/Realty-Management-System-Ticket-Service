package com.stratumtech.realtyticket.service;

import java.util.Map;

import com.stratumtech.realtyticket.dto.UserRegistrationDTO;

public interface RegistrationService {

    Map<String, Object> register(UserRegistrationDTO dto);

}
