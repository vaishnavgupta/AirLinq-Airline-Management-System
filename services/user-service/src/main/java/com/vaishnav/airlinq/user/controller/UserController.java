package com.vaishnav.airlinq.user.controller;

import com.vaishnav.airlinq.user.service.UserService;
import com.vaishnav.payload.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService  userService;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByEmail(
            @RequestHeader("X-User-Email") String email
    ) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
