package com.vaishnav.airlinq.user.mapper;

import com.vaishnav.airlinq.user.model.User;
import com.vaishnav.payload.dto.UserDto;

import java.util.List;

public class UserMapper {

    public static UserDto getDto(User  user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(user.getRole())
                .lastLogin(user.getLastLogin())
                .build();
    }

    public static List<UserDto> getDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::getDto)
                .toList();
    }

}
