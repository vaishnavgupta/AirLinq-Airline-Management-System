package com.vaishnav.airlinq.user.service.impl;

import com.vaishnav.airlinq.user.mapper.UserMapper;
import com.vaishnav.airlinq.user.model.User;
import com.vaishnav.airlinq.user.repository.UserRepository;
import com.vaishnav.airlinq.user.service.UserService;
import com.vaishnav.payload.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return UserMapper.getDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserMapper.getDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(UserMapper::getDto)
                .toList();
    }

}
