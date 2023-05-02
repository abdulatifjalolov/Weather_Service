package com.example.weather_service.repository;

import com.example.weather_service.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity,Long> {
    boolean existsByName(String name);
}
