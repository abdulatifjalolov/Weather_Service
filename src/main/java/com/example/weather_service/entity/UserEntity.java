package com.example.weather_service.entity;


import com.example.weather_service.dto.request.UserRequestDTO;
import com.example.weather_service.entity.Enum.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "users")
@NoArgsConstructor(force = true)
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private List<RoleEnum> roleEnumList;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<CityEntity> cityEntities;


    public static UserEntity of(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.isUser()) {
            return UserEntity
                    .builder()
                    .email(userRequestDTO.getEmail())
                    .password(userRequestDTO.getPassword())
                    .phoneNumber(userRequestDTO.getPhoneNumber())
                    .roleEnumList(List.of(RoleEnum.USER))
                    .build();
        }
        return UserEntity
                .builder()
                .email(userRequestDTO.getEmail())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .password(userRequestDTO.getPassword())
                .roleEnumList(userRequestDTO.getRoles())
                .build();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roleEnumList.forEach((role) ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()))
                );
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
