package com.example.weather_service.entity;

import com.example.weather_service.dto.request.CityRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "city")
@NoArgsConstructor(force = true)
public class CityEntity extends BaseEntity {
    @NotNull
    private boolean isVisible;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(nullable = false)
    private Double tempC;

    public static CityEntity of(CityRequestDTO cityRequestDTO) {
        return CityEntity.builder()
                .isVisible(cityRequestDTO.isVisible())
                .tempC(cityRequestDTO.getTempC())
                .name(cityRequestDTO.getName())
                .build();
    }
}
