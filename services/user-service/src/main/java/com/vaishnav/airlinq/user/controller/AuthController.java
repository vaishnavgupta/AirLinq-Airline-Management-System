package com.vaishnav.airlinq.user.controller;

import com.vaishnav.airlinq.user.service.AuthService;
import com.vaishnav.payload.dto.UserDto;
import com.vaishnav.payload.request.LoginRequest;
import com.vaishnav.payload.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signupUser(@Valid @RequestBody UserDto userDto) throws Exception {
        return authService.signup(userDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse loginUser(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

}
