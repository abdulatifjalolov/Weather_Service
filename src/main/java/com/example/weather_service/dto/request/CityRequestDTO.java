package com.example.weather_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityRequestDTO {
    @NotNull
    private boolean isVisible;
    @NotBlank(message = "name required")
    private String name;
    @NotNull(message = "temperature required")
    private Double tempC;
}
