package com.example.weather_service.controller;

import com.example.weather_service.dto.request.UserLoginDTO;
import com.example.weather_service.dto.request.UserRequestDTO;
import com.example.weather_service.dto.response.ApiResponse;
import com.example.weather_service.entity.UserEntity;
import com.example.weather_service.security.SwaggerConfig;
import com.example.weather_service.security.utils.TokenGenerator;
import com.example.weather_service.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = SwaggerConfig.BEARER)
public class AuthController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final TokenGenerator tokenGenerator;

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    private final JwtAuthenticationProvider refreshTokenAuthProvider;

    public AuthController(UserDetailsServiceImpl userDetailsServiceImpl,
                          TokenGenerator tokenGenerator,
                          DaoAuthenticationProvider daoAuthenticationProvider,
                          @Qualifier("jwtRefreshTokenAuthProvider") JwtAuthenticationProvider refreshTokenAuthProvider) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.tokenGenerator = tokenGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "for registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> register(
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) {
        UserEntity userEntity = UserEntity.of(userRequestDTO);
        userDetailsServiceImpl.createUser(userEntity);
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(userEntity, userEntity.getPassword(), userEntity.getAuthorities());
        return new ApiResponse<>(
                null,
                tokenGenerator.createToken(authentication)
        );
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "to get refresh and access tokens")
    public ApiResponse<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) {
        Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
        return new ApiResponse<>(
                null,
                tokenGenerator.createToken(authentication)
        );
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "to refresh tokens by refresh token")
    public ApiResponse<?> token(
            @RequestBody String refreshToken
    ) {
        Authentication authentication =
                refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(refreshToken));
//        Jwt jwt = (Jwt) authentication.getCredentials();
//         check if present in db and not revoked, etc
        return new ApiResponse<>(
                null,
                tokenGenerator.createToken(authentication)
        );
    }
}
