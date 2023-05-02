package com.example.weather_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
