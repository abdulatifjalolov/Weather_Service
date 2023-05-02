package com.example.weather_service.common.exception;

public class CityNameNotFoundException extends RuntimeException {
    public CityNameNotFoundException(String message) {
        super(message);
    }
}
