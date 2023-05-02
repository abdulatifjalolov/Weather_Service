package com.example.weather_service.service;

import com.example.weather_service.common.exception.RecordNotFountException;
import com.example.weather_service.dto.request.CityRequestDTO;
import com.example.weather_service.dto.response.UserResponseDTO;
import com.example.weather_service.entity.CityEntity;
import com.example.weather_service.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public CityEntity get(Long id) {
        return cityRepository.findById(id).orElseThrow(()->
            new RecordNotFountException(String.format("City not found for {%s}"))
        );
    }

    public List<CityEntity> list() {
        return cityRepository.findAll();
    }

    public void create(CityRequestDTO cityRequestDTO) {
        if (!cityRepository.existsByName(cityRequestDTO.getName())) {
            CityEntity cityEntity = CityEntity.of(cityRequestDTO);
            cityRepository.save(cityEntity);
            return;
        }
        throw new IllegalArgumentException(MessageFormat.format("Conflict for {0}",cityRequestDTO.getName()));
    }

    public CityEntity update(Long id, CityRequestDTO cityRequestDTO) {
        CityEntity data = cityRepository.findById(id).orElseThrow(() ->
            new RecordNotFountException(String.format("City not found for %s", id))
        );
        Long originalId = data.getId();
        CityEntity cityEntity = CityEntity.of(cityRequestDTO);
        cityEntity.setId(originalId);
        return cityRepository.save(cityEntity);
    }

    public CityEntity updateTemp (Long id, Double tempC) {
        CityEntity data = cityRepository.findById(id).orElseThrow(() ->
                new RecordNotFountException(String.format("City not found for %s", id))
        );
        data.setTempC(tempC);
        return cityRepository.save(data);
    }

}
