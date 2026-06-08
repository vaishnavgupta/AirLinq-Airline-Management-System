package com.vaishnav.airlinq.airline.client;

import com.vaishnav.payload.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable Long id) throws Exception;

}
