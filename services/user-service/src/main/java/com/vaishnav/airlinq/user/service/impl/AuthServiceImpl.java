package com.vaishnav.airlinq.user.service.impl;

import com.vaishnav.airlinq.user.mapper.UserMapper;
import com.vaishnav.airlinq.user.model.User;
import com.vaishnav.airlinq.user.repository.UserRepository;
import com.vaishnav.airlinq.user.security.JwtUtils;
import com.vaishnav.airlinq.user.service.AuthService;
import com.vaishnav.enums.UserRole;
import com.vaishnav.payload.dto.UserDto;
import com.vaishnav.payload.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public AuthResponse signup(UserDto userDto) throws Exception {
        boolean isPresent = userRepository.findByEmail(userDto.getEmail()).isPresent();
        if (isPresent) {
            throw new Exception("User already exists with email: " + userDto.getEmail());
        }
        if (userDto.getRole().equals(UserRole.ROLE_SYSTEM_ADMIN)) {
            throw new Exception("User cannot exists as System Administrator ");
        }
        User user = User.builder()
                .fullName(userDto.getFullName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phone(userDto.getPhone())
                .role(userDto.getRole())
                .lastLogin(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Authentication auth = authenticate(
                userDto.getEmail(),
                userDto.getPassword()
        );

        String jwt = jwtUtils.generateJwtToken(auth, user.getId());

        return AuthResponse.builder()
                .token(jwt)
                .title("Welcome to Airlinq")
                .user(UserMapper.getDto(user))
                .message("User registered successfully!")
                .build();
    }

    @Override
    public AuthResponse login(String email, String password) throws Exception {
        Authentication authentication = authenticate(email, password);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found with email: LoginMethod" + email));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String jwt = jwtUtils.generateJwtToken(authentication, user.getId());

        return AuthResponse.builder()
                .token(jwt)
                .title("Welcome Back to Airlinq")
                .user(UserMapper.getDto(user))
                .message("User logged in successfully!")
                .build();
    }

    private Authentication authenticate(String email, String password) throws Exception{
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials - Password Mismatch");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
