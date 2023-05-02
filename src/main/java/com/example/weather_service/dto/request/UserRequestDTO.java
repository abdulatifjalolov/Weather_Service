package com.example.weather_service.dto.request;




import com.example.weather_service.entity.Enum.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Size(min = 13, max = 13,message = "Phone number length equal to 13")
    private String phoneNumber;

    private List<RoleEnum> roles;
    @JsonIgnore
    public boolean isUser() {
        return roles == null;
    }
}

