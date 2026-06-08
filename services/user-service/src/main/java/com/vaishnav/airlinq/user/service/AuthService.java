package com.vaishnav.airlinq.user.service;

import com.vaishnav.payload.dto.UserDto;
import com.vaishnav.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String email, String password) throws Exception;

    AuthResponse signup(UserDto userDto) throws Exception;
}
