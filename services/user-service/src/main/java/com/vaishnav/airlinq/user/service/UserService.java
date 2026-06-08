package com.vaishnav.airlinq.user.service;

import com.vaishnav.payload.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserByEmail(String email);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
}
