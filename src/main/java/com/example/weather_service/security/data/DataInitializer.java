package com.example.weather_service.security.data;

import com.example.weather_service.entity.Enum.RoleEnum;
import com.example.weather_service.entity.UserEntity;
import com.example.weather_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        //add ADMIN when application is starting
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            UserEntity admin = UserEntity.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin12345"))
                    .roleEnumList(Collections.singletonList(RoleEnum.ADMIN))
                    .build();
            userRepository.save(admin);
        }
    }
}
