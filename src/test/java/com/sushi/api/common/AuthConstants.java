package com.sushi.api.common;

import com.sushi.api.model.dto.login.LoginRequestDTO;
import com.sushi.api.model.dto.login.RegisterRequestDTO;

import static com.sushi.api.common.CustomerConstants.EMAIL;
import static com.sushi.api.common.CustomerConstants.PASSWORD;

public class AuthConstants {
    public static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(EMAIL, PASSWORD);
    public static final RegisterRequestDTO REGISTER_REQUEST_DTO = new RegisterRequestDTO("Mario", "mario@gmail.com", PASSWORD);
}