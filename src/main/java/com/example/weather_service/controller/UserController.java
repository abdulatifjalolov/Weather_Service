package com.example.weather_service.controller;

import com.example.weather_service.dto.request.UserRequestDTO;
import com.example.weather_service.dto.response.ApiResponse;
import com.example.weather_service.entity.UserEntity;
import com.example.weather_service.security.SwaggerConfig;
import com.example.weather_service.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = SwaggerConfig.BEARER)
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "for ADMIN users list")
    public ApiResponse<?> list() {
        return new ApiResponse<>(
                null,
                userDetailsService.list()
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "for ADMIN update user")
    public ApiResponse<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) {
        return new ApiResponse<>(
                null,
                userDetailsService.updateUser(id, userRequestDTO)
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "for ADMIN getUserSubscribeCities")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getUserSubscribeCities(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                null,
                userDetailsService.getUserSubscribeCities(id)
        );
    }

    @PostMapping("/{cityId}")
    @Operation(summary = "subscribe to city")
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribeToCity(
            @PathVariable Long cityId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && (authentication.getPrincipal() instanceof UserEntity principal))
            userDetailsService.subscribeToCity(principal,cityId);
    }
}
