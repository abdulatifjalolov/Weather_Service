package com.example.weather_service.controller;

import com.example.weather_service.dto.request.CityRequestDTO;
import com.example.weather_service.dto.response.ApiResponse;
import com.example.weather_service.entity.UserEntity;
import com.example.weather_service.security.SwaggerConfig;
import com.example.weather_service.service.CityService;
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
@RequiredArgsConstructor
@RequestMapping("/api/city")
@SecurityRequirement(name = SwaggerConfig.BEARER)
public class CityController {

    private final CityService cityService;
    private final UserDetailsServiceImpl userDetailsService;


    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "for ADMIN citys list")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> list() {
        return new ApiResponse<>(
                null,
                cityService.list()
        );
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "for ADMIN update city")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> update(
            @PathVariable Long id,
            @Valid @RequestBody CityRequestDTO cityRequestDTO
    ) {

        return new ApiResponse<>(
                null,
                cityService.update(id, cityRequestDTO)
        );
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "for ADMIN create city")
    public void create(
            @Valid @RequestBody CityRequestDTO cityRequestDTO
    ) {
        cityService.create(cityRequestDTO);
    }

    @PutMapping("/update-weather/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "for ADMIN update city's weather")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> updateWeather(
            @PathVariable Long id,
            @RequestParam Double tempC
    ) {
        return new ApiResponse<>(
                null,
                cityService.updateTemp(id, tempC)
        );
    }

    @GetMapping("/data")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "cuurent user subscribedCitiesList")
    public ApiResponse<?> subscribedCitiesList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && (authentication.getPrincipal() instanceof UserEntity principal))
            return new ApiResponse<>(
                    null,
                    userDetailsService.getCurrentUserSubscribeCities(principal.getId())
            );
        return null;
    }
}
